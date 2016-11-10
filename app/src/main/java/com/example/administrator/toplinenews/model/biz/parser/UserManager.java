package com.example.administrator.toplinenews.model.biz.parser;

import android.webkit.URLUtil;

import com.example.administrator.toplinenews.commons.OkHttpUtil;
import com.example.administrator.toplinenews.commons.URLErrorException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 管理用户登入，注册的请求网络的实现
 */
public class UserManager {
    public String login(String urlPath) throws IOException, URLErrorException {
        return requestNetByUrl(urlPath);
    }
    /**
     *
     */

    public String register(String urlPath) throws IOException, URLErrorException {
        return requestNetByUrl(urlPath);
    }

    public String getUserInfo(String urlPath) throws IOException, URLErrorException {
        return requestNetByUrl(urlPath);
    }

    /**
     * 忘记密码
     *
     * @param urlPath
     * @return
     * @throws IOException
     * @throws URLErrorException
     */
    public String forgetPwd(String urlPath) throws IOException, URLErrorException {
        return requestNetByUrl(urlPath);
    }

    private String requestNetByUrl(String urlPath) throws IOException, URLErrorException {
        if (URLUtil.isValidUrl(urlPath)) {
            return OkHttpUtil.getString(urlPath);
        } else {
            throw new URLErrorException("url路径有问题...");
        }
    }

}
