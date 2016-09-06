package com.example.administrator.toplinenews.ui.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.HttpClientUtil;
import com.example.administrator.toplinenews.common.LogUtil;
import com.example.administrator.toplinenews.model.biz.parser.ParserNews;
import com.example.administrator.toplinenews.model.dao.NewsDBManager;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.ActivityShow;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class ActivityMain  extends MyBaseActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private NewsAdaper adapter;
    private NewsDBManager dbManager;
    //  解析数据 json
    private ParserNews newsParser;
    Message m;
    ProgressDialog dialog;
    //  每页显示 10  行
    private int limit = 10;
    //  第几页
    private int offset;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                ArrayList<News> parser =(ArrayList<News>) msg.obj;
                //  存在数据库中
                for (int i = 0; i < parser.size(); i++)
                {
                    dbManager.insertNews(parser.get(i));
                }
                loadDataFromDB(limit, offset);
            } else if (msg.what == 200) {
                showToast(" 网络连接错误 ");
            }
            dialog.dismiss();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        listView = (ListView) findViewById(R.id.listview);
        dbManager = NewsDBManager.getNewsDBManager(this);
        //  先加载缓存的新闻
        adapter = new NewsAdaper(this, listView);
        //  先加载缓存的新闻

        listView.setAdapter(adapter);
       //  滑动事件
        listView.setOnScrollListener(onScrollListener);
       //  单击事件跳转
        listView.setOnItemClickListener(this);
        if (dbManager.getCount() > 0) {
      //  数据库加载
            loadDataFromDB(limit, offset);
        }
        else {
//  网络异步加载数据
            loadData();
        }
    }


    private void loadDataFromDB(int limit2, int offset2) {
//  第一次是第 0  页 10  行
        ArrayList<News> data = dbManager.queryNews(limit2, offset2);
        adapter.addendData(data, false);
        adapter.update();
        this.offset = offset + data.size();
    }
    /**
     *  异步加载数据
     */
    private void loadData() {
        dialog = ProgressDialog.show(this, null, " 加载中，请稍候。。。 ");
      // 启动新线程加载数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://192.168.2.14:8080/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
                try {
                    //newsParser = new ParserNews(ActivityMain.this);
         //  发送请求，得到返回数据
                    String httpResponse = HttpClientUtil.httpGet(path);
                    LogUtil.d(" 请求返回的数据 ", httpResponse);
        //  将返回的数据解析
                    JSONObject object = new JSONObject(httpResponse);
                    String s = object.toString();
                    if (object.getString("message").equals("OK"))
                    {
                       // ArrayList<News> parser = newsParser.parser(object);
                        List<News> parser = ParserNews.parserNewsList(s);
                        m=new Message();
                        m.obj=parser;
                        m.what=100;
                        handler.sendMessage(m);
                    }
                    else {
                        m.what=200;
                        handler.sendMessage(m);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
        //  出错
                    handler.sendEmptyMessage(200);
                }  }
        }).start();
    }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    LogUtil.d(" 点击子条目 "+position);
                   Toast.makeText(getApplicationContext(),"123",Toast.LENGTH_LONG).show();
                    News news = adapter.getAdapterData().get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("news", news);
                    openActivity(ActivityShow.class, bundle);


                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(intent,bundle);
                    }*/

                    /*Intent intent=new Intent(ActivityMain.this,ActivityShow.class);
                    startActivity(intent);*/
                }

    /**
     *  滑动事件
     */
    private AbsListView.OnScrollListener onScrollListener = new
            AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState)
                {


                }
                //  滑动时触发的方法
                // totalItemCount  总数量
                // firstVisibleItem  第一行

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (totalItemCount > 20
                            && listView.getLastVisiblePosition() + 1 ==
                            totalItemCount) {
                        loadDataFromDB(limit, offset);
                    }
                }
            };
}
