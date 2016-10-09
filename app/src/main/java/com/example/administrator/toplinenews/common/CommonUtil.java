package com.example.administrator.toplinenews.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class CommonUtil
{
    /**  联网的 ip */
    public static String NETIP = "118.244.212.82";
    /**  联网的路径 */
    public static final int VERSION_CODE = 1;// 当前版本号
    public static String NETPATH = "http://" + NETIP + ":9092/newsClient";
    /** SharedPreferences  保存用户名键 */
    public static final String SHARE_USER_NAME = "userName";
    /** SharedPreferences  保存用户名密码 */
    public static final String SHARE_USER_PWD = "userPwd";
    /** SharedPreferences  保存是否第一次登陆 */
    public static final String SHARE_IS_FIRST_RUN = "isFirstRun";
    /** SharedPreferences  存储路径 */
    public static final String SHAREPATH = "news_share";





    public static void hideKeyBoard(Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     *  获取当前 时间
     * @return 2014-07-16 08 ：10 ：10 20140716081010
     */
    public static String getSystime()
    {
        String systime;
        // 对应的时间格式
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEEE");
       // 获取当前时间并刷格式
        systime=dateFormat.format(new Date(System.currentTimeMillis()));
        return systime;
    }

    /**
     *  获取当前日期
     * @return 20140716
     */
    public static String getDate() {
        Date date = new Date(System.currentTimeMillis());
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    /**
     *  验证邮箱格式
     * @param email email
     * @return  格式是否正确
     */
    public static boolean verifyEmail(String email){
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" +"|(([a-zA-Z0-9\\-]+\\.)+))"
                +"([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /***
     *  验证密码格式
     * @param password
     * @return
     */
    public static boolean verifyPassword(String password){
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9]{6,16}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    /**
     * 获取当前的版本号
     *
     * @param context 上下文对象
     * @return 当前版本
     */
    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
}

