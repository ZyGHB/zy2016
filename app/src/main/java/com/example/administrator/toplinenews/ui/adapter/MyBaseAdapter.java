package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public  abstract  class MyBaseAdapter<E> extends BaseAdapter {
    protected Context context;
    // 定义布局过滤器
    protected LayoutInflater inflater;
    protected List<E> myList = new ArrayList<E>();// 定义数据集合，并初始化

    public MyBaseAdapter(Context context) {
        // 初始化 context ，inflate  对象
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    protected ArrayList<E> list = new ArrayList<E>();

    /* 略*/
// 清除所有数据
    public void clear() {
        myList.clear();
    }

    // 查找所有数据
    public List<E> getAdapterData() {

        return list;
    }

    public void appendData(E e, boolean isClearOld) {
        if (e == null) { // 非空验证
            return;
        }
        if (isClearOld) {// 如果 true  清空原数据
            list.clear();
        }// 添加一条新数据到最后
        list.add(e);
    }

    public void addendData(ArrayList<E> alist,boolean isClearOld){
        if(alist==null){
            return;
        }
        if(isClearOld){
            list.clear();
        }
        list.addAll(alist);
    }
    public void appendDataTop(E t,boolean isClearOld) {
        if (t == null) { // 非空验证
            return;
        }
        if (isClearOld) {// 如果 true  清空原数据
            list.clear();
        }
        list.add(0,t);
    }
    public void addendDataTop(ArrayList<E> alist,boolean isClearOld){
        if(alist==null){
            return;
        }
        if(isClearOld){
            list.clear();
        }
        list.addAll(0,alist);
    }

    public void update(){
        // 刷新适配器
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(list==null){
            return 0;
        }else{
            return list.size();
        }
    }
    @Override
    public E getItem(int position) {
        if(list==null){
            return null;
        }
        if(position>list.size()-1){
            return null;
        }
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMyView(position, convertView, parent);
    }
    public abstract View getMyView(int position, View convertView, ViewGroup parent);
}