package com.example.administrator.toplinenews.common;

/**
 * Created by Administrator on 2016/9/18 0018.
 */
public interface LoadCallbackListener<T> {

    void onSuccess(T t);
    void onFailed(T t);
}
