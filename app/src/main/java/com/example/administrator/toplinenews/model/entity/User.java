package com.example.administrator.toplinenews.model.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class User {
    /**  用户 id */
    private String uid;
    /**  用户邮箱 */
    private String email;
    /**  用户积分 */
    private int integration;
    /**  评论数量 */
    private int comnum;
    /**  头像 */
    private String portrait;
    /**  登陆日志 */
    private List<LoginLog> loginlog;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public int getIntegration() {
        return integration;
    }

    public int getComnum() {
        return comnum;
    }

    public String getPortrait() {
        return portrait;
    }

    public List<LoginLog> getLoginlog() {
        return loginlog;
    }
}
