package com.example.administrator.toplinenews.common;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class Const {
    private static final String URL = "http://118.244.212.82:9092/newsClient/";
    public static final String PHONE = "0";
    //新闻的请求路径
    public static final String URL_NEW_LIST = URL+"news_list?";
    //登入的请求路径
    public static final String URL_LOGIN = URL+"user_login?";
    //register
    public static final String URL_REGISTER = URL+"user_register?";
    //forget pwd /user_forgetpass?ver=" + args[0] + "&email=" + args[1]
    public static final String URL_FORGET_PWD = URL+"user_forgetpass?";
    public static final String URL_USER_INFOR = URL+"user_home?";
    //user_image?token=用户令牌& portrait =头像
    public static final String URL_USER_IMAGE = URL+"user_image?";
    public static final String URL_BIG_MAP = URL+"news_image?";
    //用户评论
    public static final String URL_USER_COMMENT = URL + "cmt_commit?";
    //用户评论数量
    public static final String URL_USER_COMMENT_COUNT = URL + "cmt_num?";
    //用户评论信息
    public static final String URL_USER_COMMENT_INFO = URL + "cmt_list?";
}
