package com.example.administrator.toplinenews.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.toplinenews.common.LogUtil;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class MyBaseActivity extends AppCompatActivity {
    protected int screen_w,screen_h;
    /* 略*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
/* 略*/
        super.onCreate(savedInstanceState);
        screen_w=getWindowManager().getDefaultDisplay().getWidth();
        screen_h=getWindowManager().getDefaultDisplay().getHeight();
/* 略*/
    }
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(LogUtil.TAG,getClass().getSimpleName()+"onStart()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void openActivity(Class<?> pClass,Bundle bundle,Uri uri)
    {
        Intent intent=new Intent(this, pClass);
        if(bundle!=null)
            intent.putExtras(bundle);
        if(uri!=null)
            intent.setData(uri);
        startActivity(intent);
    }
    public void openActivity(Class<?> pClass,Bundle bundle){
        openActivity(pClass, bundle, null);
    }
    //通过 class  名字进行界面跳转
    public void openActivity(Class<?> pClass){
        openActivity(pClass, null, null);
    }
    // 通过 action  字符串进行界面跳转
    public void openActivity(String action){
        openActivity(action, null, null);
    }
    // 通过 action  字符串进行界面跳转，只带 Bundle  数据
    public void openActivity(String action,Bundle bundle){
        openActivity(action, bundle, null);
    }
    // 通过 action  字符串进行界面跳转，并带 Bundle 和 和 Url  数据
    public void openActivity(String action,Bundle bundle,Uri uri){
        Intent intent=new Intent(action);
        if(bundle!=null)
            intent.putExtras(bundle);
        if(uri!=null)
            intent.setData(uri);
        startActivity(intent);
    }
    private Toast toast;
    public void showToast(int resId)
    {
        showToast(getString(resId));
    }
    public void showToast(String msg){
        if(toast==null)
            toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
}
