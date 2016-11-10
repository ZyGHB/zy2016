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
import com.example.administrator.toplinenews.commons.HttpAsyncTask;
import com.example.administrator.toplinenews.commons.LoadCallbackListener;
import com.example.administrator.toplinenews.commons.SystemUtils;
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


        if (SystemUtils.getInstance(getContext()).isNetConn()) {
            getNewsFromNet(url);
        } else {
            getNewFromLocal();
        }

    }
    private void getNewFromLocal() {
        //从数据库中读取数据
    }
    private void getNewsFromNet(String url) {
        //        //在UI线程，创建AsyncTask子类的对象
        final HttpAsyncTask task = new HttpAsyncTask(getContext());
        //开启异步任务
        task.setListener(new LoadCallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                ArrayList<News> newses = GsonParseUtil.parseNewJsonString(s);

                //Toast.makeText(getContext(), "成功", Toast.LENGTH_SHORT).show();

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
