package com.example.administrator.toplinenews.model.biz.parser;

import android.content.Context;

import com.example.administrator.toplinenews.model.entity.BaseEntity;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.model.entity.NewsType;
import com.example.administrator.toplinenews.model.entity.SubType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class ParserNews {
    private Context context;
    public ParserNews(Context context){
        this.context=context;
    }
    // 解析新闻数据
   /* public ArrayList<News> parser(JSONObject jsonObject) throws Exception{
        ArrayList<News> newsLit=new ArrayList<News>();
// 根据数据块名称 获取一个数组

        JSONArray jsonArray=jsonObject.getJSONArray("data");
// 循环遍历这个数组
        for (int i = 0; i <jsonArray.length(); i++) {
            JSONObject object=jsonArray.getJSONObject(i);
            int nid = object.getInt("nid");
            String title = object.getString("title");
            String summary = object.getString("summary");
            String icon = object.getString("icon");
            String link = object.getString("link");
            int type = object.getInt("type");
            News news=new News(nid, title, summary, icon, link,type);
            newsLit.add(news);
        }
        return newsLit;
    }*/

    /**
     * gson  解析新闻分类
     * @param json
     * @return list
     */
    public static List<SubType> parserTypeList(String json){
        Gson gson = new Gson();
        BaseEntity<List<NewsType>> typeEntity = gson.fromJson(json, new
                TypeToken<BaseEntity<List<NewsType>>>() {}.getType());
        for(NewsType type: typeEntity.getData()) {
            System.out.println(type.getSubgrp());
        }
        // 第一版没有分类列表, 暂且返回默认主分类的子分类
        return typeEntity.getData().get(0).getSubgrp();
        }
    public static List<News> parserNewsList(String json){
        Gson gson = new Gson();
        BaseEntity<List<News>> newsEntity = gson.fromJson(json, new
                TypeToken<BaseEntity<List<News>>>() {}.getType());
        return newsEntity.getData();
    }
}
