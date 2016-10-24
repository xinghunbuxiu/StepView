package com.lixh.stepview.stepview.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.lixh.stepview.stepview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 如何做到居中显示
 * 1.首先得到该控件的宽度。
 * 2.其次得到当前控件距离最左边值。然后计算当前有几个圆，几个条线？这个是外部传递进来的，告诉该控件当前共有几个步骤，并且当前正在执行到哪一步？
 * 当前控件距离最左边值，定义为：paddingLeft；
 * 当前控件的宽度：getWidth()；
 * 圆的半径：mCircleRadius；
 * 两条线之前的padding值：mLinePadding；
 */
public class HorizontalStepsViewIndicator extends View {
    private Paint mSignPaint;//已签到paint      definition mSignPaint
    public Paint mUnSignPaint;//未签到Paint  definition mUnSignPaint
    private int mUnSignPaintLineColor = ContextCompat.getColor(getContext(), R.color.color_9C);//定义默认未完成线的颜色  definition
    public int mSignPaintLineColor = ContextCompat.getColor(getContext(), R.color.color_FF);//定义默认未完成线的颜色  definition
    float paddingLeft = 0.0f;
    float mScrollX = 0.0f;
    private Drawable mSignIcon;//已签到图片    definition default mSignIcon
    private Drawable mUnSignIcon;//未签到的图片     definition default mUnSignIcon
    private Drawable mRedPackIcon;//红包的背景图  definition default mRedPackIcon
    private Drawable mIndicatorIcon;//指示器的背景图  definition default mRedPackIcon
    private List<PointBean> mStepBeanList = new ArrayList<PointBean>();//步数
    private float mLinePadding;//两条连线之间的间距  definition the spacing between the two circles
    private float mCircleRadius;// 未签到圆的半径  definition circle radius
    private float mCenterY;//该view的Y轴中间位置     definition view centerY position
    private boolean firstLoad = true;
    private int mStepNum = 0;
    private int screenWidth;//this screen width
    private int oldPosition = 0;
    int upLocation = 0;//跳转的步长

    public HorizontalStepsViewIndicator(Context context) {
        this(context, null);
    }


    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalStepsViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mUnSignPaint = new Paint();
        mUnSignPaint.setAntiAlias(true);
        mUnSignPaint.setColor(getUnSignPaintColor());
        mUnSignPaint.setStyle(Paint.Style.STROKE);
        mUnSignPaint.setStrokeWidth(4);
        mSignPaint = new Paint();
        mSignPaint.setAntiAlias(true);
        mSignPaint.setColor(getSignPaintColor());
        mSignPaint.setStyle(Paint.Style.STROKE);
        mSignPaint.setStrokeWidth(4);

        mSignIcon = ContextCompat.getDrawable(getContext(), R.mipmap.icon_sign);//已签到图片 icon
        mUnSignIcon = ContextCompat.getDrawable(getContext(), R.mipmap.icon_unsign);//未签到的图片 icon
        mRedPackIcon = ContextCompat.getDrawable(getContext(), R.mipmap.icon_redpack);//红包的背景图 icon
        mIndicatorIcon = ContextCompat.getDrawable(getContext(), R.mipmap.icon_indicator);//指示器的背景图 icon

        //圆的半径  set mCircleRadius
        mCircleRadius = mSignIcon.getIntrinsicWidth() / 2;
    }

    public int getUnSignPaintColor() {
        return mUnSignPaintLineColor;
    }

    public int getSignPaintColor() {
        return mSignPaintLineColor;
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = (mRedPackIcon.getIntrinsicHeight() * 3);
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * getHeight();
        // 将屏幕的宽平均分为6份 由于第一次显示时left 有paddingleft 宽-宽的12分之一在等分6份
        mLinePadding = firstLoad ? (screenWidth - screenWidth / 12) / 6 : screenWidth / 6;
        //控制view的移动指数
        paddingLeft = firstLoad ? mLinePadding / 2 : 0;
        if (upLocation != 0) {
            //移动的距离=步长*一部分的宽
            mScrollX = mScrollX + upLocation * mLinePadding;
            upLocation = 0;
        }
        setMeasuredDimension(screenWidth, height);
        //将所有的起点,中心，终点保存起来
        for (int i = 0; i < mStepNum; i++) {
            PointBean p = mStepBeanList.get(i);
            float startX = i * mLinePadding + paddingLeft - mScrollX;
            float circleX = mLinePadding * i + paddingLeft - mScrollX + mLinePadding / 2;
            float endX = i * mLinePadding + paddingLeft - mScrollX + mLinePadding;
            p.setStartX(startX);
            p.setEndX(endX);
            p.setCircleX(circleX);

        }
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    int currentIndicatorLeft;
    float scrollX;

    //动画移动效果
    private void moveAnimation() {
        TranslateAnimation animation = new TranslateAnimation(mLinePadding,
                0f, 0f, 0f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);
        // 执行位移动画
        startAnimation(animation);
    }


    /**
     * 设置流程步数
     *
     * @param stepsBeanList 流程步数
     */
    public HorizontalStepsViewIndicator setStepNum(List<PointBean> stepsBeanList) {
        this.mStepBeanList = stepsBeanList;
        mStepNum = mStepBeanList.size();
        requestLayout();
        return this;
    }

    public PointBean getStepBeanList(int position) {
        return mStepBeanList.get(position);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //-----------------------------draw line-----------------------------------------------
        for (int i = 0; i < mStepBeanList.size(); i++) {
            PointBean stepsBean = mStepBeanList.get(i);
            //前一个startXPosition
            final float startXPosition = stepsBean.getStartX();//起点
            //后一个endXPosition
            final float endXPosition = stepsBean.getEndX();

            float circleX = stepsBean.getCircleX();

            // -----------------------画icon----------------------
            Rect rect = new Rect((int) (circleX - mCircleRadius), (int) (mCenterY - mCircleRadius), (int) (circleX + mCircleRadius), (int) (mCenterY + mCircleRadius));
            //----------------------------------------画指示器--------------------
            Rect mIndicatorRect = new Rect((int) (circleX - mCircleRadius), (int) (mCenterY - mRedPackIcon.getIntrinsicHeight() / 2 - mIndicatorIcon.getIntrinsicHeight()), (int) (circleX + mCircleRadius), (int) (mCenterY - mRedPackIcon.getIntrinsicHeight() / 2));


            canvas.drawLine(startXPosition, mCenterY, circleX - mCircleRadius, mCenterY, mUnSignPaint);
            canvas.drawLine(circleX + mCircleRadius, mCenterY, endXPosition, mCenterY, mUnSignPaint);

            //是否当前
            if (stepsBean.isCurrent()) {
                oldPosition = i;
                mIndicatorIcon.setBounds(mIndicatorRect);
                mIndicatorIcon.draw(canvas);
            }

            //是否有红包
            if (stepsBean.isHaveRed()) {
                Rect redRect = new Rect((int) (circleX - mCircleRadius), (int) (mCenterY - mRedPackIcon.getIntrinsicHeight() / 2), (int) (circleX + mCircleRadius), (int) (mCenterY + mRedPackIcon.getIntrinsicHeight() / 2));

                mRedPackIcon.setBounds(redRect);
                mRedPackIcon.draw(canvas);
                if (stepsBean.getState() == stepsBean.STEP_SIGN) {
                    canvas.drawLine(startXPosition, mCenterY, circleX - mCircleRadius, mCenterY, mSignPaint);
                    canvas.drawLine(circleX + mCircleRadius, mCenterY, endXPosition, mCenterY, mSignPaint);
                }
            } else {
                //白线
                if (stepsBean.getState() == stepsBean.STEP_SIGN) {
                    mSignIcon.setBounds(rect);
                    mSignIcon.draw(canvas);
                    canvas.drawLine(startXPosition, mCenterY, circleX - mCircleRadius, mCenterY, mSignPaint);
                    canvas.drawLine(circleX + mCircleRadius, mCenterY, endXPosition, mCenterY, mSignPaint);
                } else {
                    mUnSignIcon.setBounds(rect);
                    mUnSignIcon.draw(canvas);
                }
            }


            //drawText-----------------------------写字
            Rect targetRect = new Rect((int) startXPosition, (int) (mCenterY + mCircleRadius + 15), (int) endXPosition, (int) (mCenterY + mCircleRadius * 2 + 15));
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(3);
            paint.setTextSize(sp2px(getContext(), 12));
            String testString = mStepBeanList.get(i).getName();
            paint.setColor(Color.WHITE);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(testString, targetRect.centerX(), baseline, paint);

        }

    }

    //执行进度
    public void signAction() {
        if (oldPosition + 1 < mStepBeanList.size() - 1) {
            mStepBeanList.get(oldPosition).setState(PointBean.STEP_SIGN);
            mStepBeanList.get(oldPosition).setCurrent(false);
            mStepBeanList.get(oldPosition + 1).setState(PointBean.STEP_SIGN);
            mStepBeanList.get(oldPosition + 1).setCurrent(true);
        }
        if (oldPosition + 1 >= 5 && mStepBeanList.size() - 1 > oldPosition + 1) {
            scrollTo();
        }
        invalidate();
    }

    //移动一天
    public void scrollTo() {
        upLocation = 1;
        firstLoad = false;
        requestLayout();
        moveAnimation();
    }

    /**
     * 移动好几步
     *
     * @param day 累计几天
     */
    public void scrollToDays(int day) {
        if (day < mStepBeanList.size() - 1) {
            for (int i = 0; i < day - 1; i++) {
                mStepBeanList.get(i).setState(PointBean.STEP_SIGN);
                mStepBeanList.get(i).setCurrent(false);
            }
            mStepBeanList.get(day - 1).setState(PointBean.STEP_SIGN);
            mStepBeanList.get(day - 1).setCurrent(true);
        }
        firstLoad = false;
        if (day - 1 >= 5 && mStepBeanList.size() - 1 > day - 1) {
            upLocation = day - 5;
        }
        requestLayout();
        invalidate();
    }

    //如果下一月重新计算
    public void resetView() {
        firstLoad = true;
        mScrollX = 0;
        upLocation = 0;
        oldPosition = 0;
        requestLayout();
        invalidate();
    }


    /**
     * 设置未签到线的颜色
     *
     * @param mUnSignPaintLineColor
     */

    public void setUnSignLineColor(int mUnSignPaintLineColor) {
        this.mUnSignPaintLineColor = mUnSignPaintLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param mSignPaintLineColor
     */
    public void setSignLineColor(int mSignPaintLineColor) {
        this.mSignPaintLineColor = mSignPaintLineColor;
    }

    /**
     * 设置默认图片
     *
     * @param mUnSignIcon
     */
    public void setUnSignIcon(Drawable mUnSignIcon) {
        this.mUnSignIcon = mUnSignIcon;
    }

    /**
     * 设置已签到图片
     *
     * @param mSignIcon
     */
    public void setSignIcon(Drawable mSignIcon) {
        this.mSignIcon = mSignIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setIndicatorIcon(Drawable attentionIcon) {
        this.mIndicatorIcon = mIndicatorIcon;
    }

}