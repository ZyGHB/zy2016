package com.example.administrator.toplinenews.model.entity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.HttpAsyncTask;
import com.example.administrator.toplinenews.common.LoadCallbackListener;
import com.example.administrator.toplinenews.model.biz.parser.GsonParseUtil;
import com.example.administrator.toplinenews.model.dao.NewsDBManager;
import com.example.administrator.toplinenews.ui.ActivityShow;
import com.example.administrator.toplinenews.ui.adapter.MySimpleAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/13 0013.
 */
public  class DummyFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    private int limit = 10;
    //  第几页
    private int offset;
    private NewsDBManager dbManager;
    int color;
    MySimpleAdapter adapter;
    Message m;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    private View container_progress;
    private View container_failed;
    private SwipeRefreshLayout refreshLayout;
    View nofile;
    View bg;
    String url =  "http://118.244.212.82:9092/newsClient/"+"news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
  /*  private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                ArrayList<News> parser =(ArrayList<News>) msg.obj;
                //  存在数据库中
                for (int i = 0; i < parser.size(); i++)
                {
                    dbManager.insertNews(parser.get(i));
                }

                //加载到适配器中
                loadDataFromDB(limit, offset);
            } else if (msg.what == 200) {
               // showToast(" 网络连接错误 ");
            }
            dialog.dismiss();
        }
    };*/

    public DummyFragment() {
    }

    @SuppressLint("ValidFragment")
    public DummyFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);

        recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        /*List<String> list = new ArrayList<String>();


        for (int i = 0; i < VersionModel.data.length; i++) {
            list.add(VersionModel.data[i]);
        }*/
       /* dbManager = NewsDBManager.getNewsDBManager(getActivity().getBaseContext());
        //  先加载缓存的新闻

        adapter = new MySimpleAdapter(getActivity().getBaseContext(),recyclerView);

        if (dbManager.getCount() > 0) {
            //  数据库加载
            loadDataFromDB(limit, offset);
        }
        else {
//  网络异步加载数据
            loadData();
        }

        recyclerView.setAdapter(adapter);

        return view;
    }
    private void loadData() {
        dialog = ProgressDialog.show(getActivity().getBaseContext(), null, " 加载中，请稍候。。。 ");
        // 启动新线程加载数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1";
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
        }).start();*/
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
         bg = view.findViewById(R.id.dummyfrag_bg);
        container_progress = view.findViewById(R.id.container_progress);
        container_failed = view.findViewById(R.id.container_failed);
        nofile = view.findViewById(R.id.container_nofile);
        TextView tv_load = (TextView) view.findViewById(R.id.tv_load);
        tv_load.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加载失败的布局消失
                        container_failed.setVisibility(View.GONE);
                        //让加载进度的界面可见
                        container_progress.setVisibility(View.VISIBLE);
                        myAsyncLoad(url);
                    }
                }
        );
        refreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimaryl),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));

        refreshLayout.setOnRefreshListener(this);

        adapter = new MySimpleAdapter(getActivity().getBaseContext(),recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MySimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, News news) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", news);
                Intent intent=new Intent(getContext(),ActivityShow.class );
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        myAsyncLoad(url);
        return view;
    }
    private void loadDataFromDB(int limit2, int offset2) {
//  第一次是第 0  页 10  行
        ArrayList<News> data = dbManager.queryNews(limit2, offset2);
        adapter.addendData(data);
        adapter.update();
        this.offset = offset + data.size();
    }

    public void myAsyncLoad(String url) {
        this.url = url;
        //        //在UI线程，创建AsyncTask子类的对象
        final HttpAsyncTask task = new HttpAsyncTask(getContext());
        //开启异步任务
        task.setListener(new LoadCallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<News> newses = GsonParseUtil.parseNewJsonString(s);

                Toast.makeText(getContext(), "成功", Toast.LENGTH_SHORT).show();

                adapter.setNewses(newses);
                adapter.notifyDataSetChanged();
                try {
                    Thread.sleep(500);

                    if(newses!=null)
                    {
                        container_progress.setVisibility(View.GONE);
                    }
                    else
                    {
                        nofile.setVisibility(View.VISIBLE);
                        container_progress.setVisibility(View.GONE);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                container_failed.setVisibility(View.VISIBLE);
                container_progress.setVisibility(View.GONE);
            }
        });
        task.execute(url);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "refresh", Toast.LENGTH_SHORT).show();
        myAsyncLoad(url);
        refreshLayout.setRefreshing(false);
    }
}
