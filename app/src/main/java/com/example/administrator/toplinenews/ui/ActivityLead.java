package com.example.administrator.toplinenews.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.adapter.LeadImgAdapter;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class ActivityLead extends MyBaseActivity {

    private ViewPager viewPager;
    private ImageView[] points=new ImageView[4];
    private LeadImgAdapter adapter;


    /** 当界面切换后调用*/
    private ViewPager.OnPageChangeListener listener=new ViewPager.OnPageChangeListener() {

        /** 界面切换时调用*/
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当界面切换后调用
         */
        @Override
        public void onPageSelected(int arg0) {
            setPoint(arg0);
            if (arg0 >= 3) {
                openActivity(ActivityMain.class);
                finish();
                //SharedPreferences是Android平台上一个轻量级的存储类，用来保存应用的一些常用配置。
                SharedPreferences preferences=getSharedPreferences("runconfig", MODE_PRIVATE);
                //共享偏好编辑器
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("isFirstRun", false);
                editor.commit();
            }
        }
        /** 滑动状态变化时调用*/
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        points[0]=(ImageView) findViewById(R.id.iv_p1);
        points[1]=(ImageView) findViewById(R.id.iv_p2);
        points[2]=(ImageView) findViewById(R.id.iv_p3);
        points[3]=(ImageView) findViewById(R.id.iv_p4);
        setPoint(0);

        viewPager=(ViewPager) findViewById(R.id.viewpager);
        // 设置每一个具体界面的样式
        List<View> viewList=new ArrayList<View>();
        viewList.add(getLayoutInflater().inflate(R.layout.lead_1, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_2, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_3, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead_4, null));
        // 初始化适配器
        adapter=new LeadImgAdapter(viewList);
        // 设置适配器
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(listener);

    }
    private void setPoint(int index) {
        for (int i = 0; i < points.length; i++) {
            if(i==index){
                //透明度，变化范围是0.0f到1.0f之间，一般用于渐变动画，或者手势滑动view的渐变效果
                points[i].setAlpha(255);
            }else{
                points[i].setAlpha(100);
            }
        }
            }


}
