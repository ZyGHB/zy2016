package com.example.administrator.toplinenews.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.CommonUtil;
import com.example.administrator.toplinenews.common.Const;
import com.example.administrator.toplinenews.common.OkHttpUtil;
import com.example.administrator.toplinenews.common.UrlComposeUtil;
import com.example.administrator.toplinenews.model.entity.BaseEntity;
import com.example.administrator.toplinenews.model.entity.Comments;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.adapter.CommentsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCommentActivity extends AppCompatActivity {

    private ListView lv;
    private News news;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);
        //初始化view
        initView();
        //得到上个界面传递过来的intent
        Intent intent = getIntent();
        if (intent != null) {
            news = (News) intent.getSerializableExtra("news");
            //下载评论
            loadComment();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.list_comment);
        adapter = new CommentsAdapter(this);
        lv.setAdapter(adapter);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadComment() {
        String  url =  geturl();
        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... params) {
                return OkHttpUtil.getString(params[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    ArrayList<Comments> data = parseComment(s);
                    adapter.appendData(data,false);
                    List<Comments> adapterData = adapter.getAdapterData();
                    adapterData.size();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ShowCommentActivity.this, "网络异常...", Toast.LENGTH_SHORT).show();
                }
            }

            private ArrayList<Comments> parseComment(String s) {
                Gson gson = new Gson();
                Type type = new TypeToken<BaseEntity<ArrayList<Comments>>>() {
                }.getType();
                BaseEntity entity = gson.fromJson(s, type);
                return (ArrayList<Comments>) entity.getData();
            }
        }.execute(url);
    }


    public String geturl() {
        /*
        cmt_list ?ver=版本号&nid=新闻id&type=1&stamp=yyyyMMdd&cid=评论id&dir=0&cnt=20
         */
        Map<String, String> p = new HashMap<>();
        p.put("ver", CommonUtil.getVersionCode(this) + "");
        p.put("nid", news.getNid()+"");
        p.put("type", "1");
        p.put("stamp", "20111111");//来一个选择框可以选择查看什么时候的评论
        p.put("cid", "1");
        p.put("dir", "1");
        p.put("cnt", "20");
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_USER_COMMENT_INFO, p);
        return urlPath;
    }
}
