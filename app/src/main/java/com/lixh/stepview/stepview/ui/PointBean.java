package com.lixh.stepview.stepview.ui;

import java.io.Serializable;

/**
 * Created by lixh on 2016/11/8.
 */

public class PointBean implements Serializable {
    public static final int STEP_SIGN = 1;//已签到  undo step
    public static final int STEP_UNSIGN = 0;//未签到 current step
    private String name;
    private int state;
    float startX; //绘制线的起点
    float endX;//绘制线的终点
    float circleX;// 绘制圆心
    private boolean isHaveRed = false;


    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    private boolean isCurrent = false;

    /**
     * @param name     底部显示文字
     * @param stepSign 状态 签到or
     */
    public PointBean(String name, int stepSign) {
        this.name = name;
        this.state = stepSign;
    }

    public boolean isHaveRed() {
        return isHaveRed;
    }

    public void setHaveRed(boolean haveRed) {
        isHaveRed = haveRed;
    }

    /**
     * @param name      底部显示文字
     * @param stepSign  状态 签到or
     * @param isCurrent 指示箭头 在那个位置
     */
    public PointBean(String name, int stepSign, boolean isCurrent) {
        this.name = name;
        this.state = stepSign;
        this.isCurrent = isCurrent;
    }

    /**
     * @param name      底部显示文字
     * @param stepSign  状态 签到or
     * @param isCurrent 指示箭头 在那个位置
     * @param isHaveRed 是否是红包
     */
    public PointBean(String name, int stepSign, boolean isCurrent, boolean isHaveRed) {
        this.name = name;
        this.state = stepSign;
        this.isCurrent = isCurrent;
        this.isHaveRed = isHaveRed;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getCircleX() {
        return circleX;
    }

    public void setCircleX(float circleX) {
        this.circleX = circleX;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
