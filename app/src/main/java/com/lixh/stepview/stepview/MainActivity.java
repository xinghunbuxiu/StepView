package com.lixh.stepview.stepview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lixh.stepview.stepview.ui.HorizontalStepsViewIndicator;
import com.lixh.stepview.stepview.ui.PointBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    HorizontalStepsViewIndicator stepsView;
    List<PointBean> stepsBeanList = new ArrayList<PointBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        stepsView = (HorizontalStepsViewIndicator) findViewById(R.id.stepView);
        stepsBeanList.add(new PointBean("第1天", PointBean.STEP_SIGN, false, true));
        stepsBeanList.add(new PointBean("第2天", PointBean.STEP_SIGN));
        stepsBeanList.add(new PointBean("第3天", PointBean.STEP_SIGN, true));
        stepsBeanList.add(new PointBean("第4天", PointBean.STEP_UNSIGN));
        stepsBeanList.add(new PointBean("第5天", PointBean.STEP_UNSIGN));
        stepsBeanList.add(new PointBean("第6天", PointBean.STEP_UNSIGN));
        stepsBeanList.add(new PointBean("第7天", PointBean.STEP_UNSIGN));
        stepsBeanList.add(new PointBean("第8天", PointBean.STEP_UNSIGN));
        stepsBeanList.add(new PointBean("第9天", PointBean.STEP_UNSIGN));
        stepsView.setStepNum(stepsBeanList);
        //  stepsView.scrollToDays(6); 跳到指定的位置
    }

    public void onSign(View view) {
        stepsView.signAction();
    }
}
