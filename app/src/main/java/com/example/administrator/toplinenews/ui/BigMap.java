package com.example.administrator.toplinenews.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.CommonUtil;
import com.example.administrator.toplinenews.common.Const;
import com.example.administrator.toplinenews.common.OkHttpUtil;
import com.example.administrator.toplinenews.common.UrlComposeUtil;
import com.example.administrator.toplinenews.model.entity.BaseEntity;
import com.example.administrator.toplinenews.model.entity.BigmapUit;
import com.example.administrator.toplinenews.model.entity.NewsGroup;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.adapter.BigmapAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BigMap extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView list;
    Context context;
    String TAG="BigMap";
    private List<String> titles = new ArrayList<>();
    private List<Integer> subids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_map);
        toolbar = (Toolbar) findViewById(R.id.toolbar_bigmap);
        list= (RecyclerView) findViewById(R.id.bigmap_list);
        context=BigMap.this;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Toast.makeText(context,"1234",Toast.LENGTH_LONG).show();
        asynLoadData();

    }
    private void asynLoadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseNewsGroupJsonString();
                Map<String, String> p = new HashMap<>();
                p.put("ver", CommonUtil.getVersionCode(context) + "");
                p.put("nid", 2+"");
                String urlPath = UrlComposeUtil.getUrlPath(Const.URL_BIG_MAP, p);
                String s = OkHttpUtil.getString(urlPath);
                Log.d(TAG, " " + s);
                if (s != null && s.contains("OK")) {
                    //成功，解析数据
                    List<BigmapUit> bigNewses = paserBigmap(s);
                    //将数据添加到适配器，刷新适配器，必须在主线程进行
                    //把数据显示到rv中
                    setDatatoAdp(bigNewses);
                } else {
                    //失败,tusi
                }

            }
        }).start();
    }
    private void setDatatoAdp(final List<BigmapUit> bigmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//initView ,必须在主线程中执行
                BigmapAdapter  adapter = new BigmapAdapter(context,bigmap);
                list.setLayoutManager(new LinearLayoutManager(context));
                list.setAdapter(adapter);

            }
        });
    }

    private List<BigmapUit> paserBigmap(String s) {
        Gson g=new Gson();
        Type t=new TypeToken<BaseEntity<List<BigmapUit>>>(){}.getType();
        BaseEntity baseentity = g.fromJson(s, t);
        return (List<BigmapUit>)baseentity.getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        ActivityMain.setmune();
        finish();
        return super.onSupportNavigateUp();
    }
    public void parseNewsGroupJsonString() {
        String url = "http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1";
        String data = HttpUtil.getJsonString(url);
        if (data == null) {
            //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "onCreate: " + data);

        Gson gson = new Gson();
        Type type = new TypeToken<NewsGroup<List<NewsGroup.DataBean<List<NewsGroup.DataBean.SubgrpBean>>>>>() {
        }.getType();

        NewsGroup newsGroup = gson.fromJson(data, type);
        Log.d(TAG, "parseNewsGroupJsonString: " + newsGroup.getMessage());
        List<NewsGroup.DataBean> data1 = (List<NewsGroup.DataBean>) newsGroup.getData();
        for (NewsGroup.DataBean dataBean : data1) {
            String group = dataBean.getGroup();
            Log.d(TAG, "group: " + group);
            List<NewsGroup.DataBean.SubgrpBean> subgrp = (List<NewsGroup.DataBean.SubgrpBean>) dataBean.getSubgrp();
            for (NewsGroup.DataBean.SubgrpBean subgrpBean : subgrp) {
                Log.d(TAG, "sub grp: "+subgrpBean.getSubid());
                titles.add(subgrpBean.getSubgroup());
                subids.add(subgrpBean.getSubid());
            }
        }

    }
}
