package com.example.administrator.toplinenews.model.entity;

/**
 * Created by Administrator on 2016/9/5 0005.
 */

/**
 *  有关注册和登录的 entity
 *
 * @author qinyq
 *
 */
public class Register
{
    //服务器返回结果
   private String result;
    // 用户令牌，用于验证用户。每次请求都传递给服务器。具有时效期。
   private String token;
    // 服务器返回结果说明
  private String explain;

    public String getResult() {
        return result;
    }

    public String getToken() {
        return token;
    }

    public String getExplain() {
        return explain;
    }
}
