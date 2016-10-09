package com.example.administrator.toplinenews.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class SharedPreferenceUtil {
    public static void saveToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        preferences.edit().putString("token", token).apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        return preferences.getString("token","");
    }

    public static void saveUserName(Context context, String username) {
        SharedPreferences preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        preferences.edit().putString("username", username).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    public static void saveAccount(Context context, String username,String password) {
        SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        preferences.edit().putString("username", username).putString("password",password).apply();

    }
    public static String[] getAccount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
        String[] account =new String[2];
        account[0]=preferences.getString("username",null);
        account[1]=preferences.getString("password", null);
        return  account;
    }

}
