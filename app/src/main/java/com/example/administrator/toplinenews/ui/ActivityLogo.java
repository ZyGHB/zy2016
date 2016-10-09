package com.example.administrator.toplinenews.ui;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;

/**
 * Created by Administrator on 2016/9/26 0026.
 */
public class ActivityLogo extends MyBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        ImageView logo = (ImageView) findViewById(R.id.iv_logo);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.logo);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            // 动画启动时调用
            @Override
            public void onAnimationStart(Animation animation) {
            }

            // 动画重复时调用
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画结束时调用
            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(ActivityMain.class);
                ActivityLogo.this.finish();
            }
        });
        logo.setAnimation(animation);
    }
}




