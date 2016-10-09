package com.example.administrator.toplinenews.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.dao.LocalNewsDBManager;
import com.example.administrator.toplinenews.model.dao.LocalNewsSQLiteOP;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.adapter.MyRecycleViewAdapter;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class CollectActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv= (RecyclerView) findViewById(R.id.recyclerlist);
        toolbar.setTitle("我的收藏");
        setSupportActionBar(toolbar);
        context = CollectActivity.this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final ArrayList<News> newses = new ArrayList<>();
        final MyRecycleViewAdapter adapter = new MyRecycleViewAdapter(newses, context);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        new Thread() {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocalNewsSQLiteOP localNewsSQLiteOP=new LocalNewsSQLiteOP(context,
                                LocalNewsSQLiteOP.DATABASE_NAME,
                                null,
                                LocalNewsSQLiteOP.DATABASE_VERSION);
                        final ArrayList<News> newses = LocalNewsDBManager.query(localNewsSQLiteOP);
                        adapter.setNewses(newses);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        ActivityMain.setmune();
        return super.onSupportNavigateUp();
    }



}
