package com.example.administrator.toplinenews.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.dao.LocaCommentDBM;
import com.example.administrator.toplinenews.model.dao.LocalCommentSQL;
import com.example.administrator.toplinenews.model.entity.Comments;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.adapter.MyCommentRcy;

import java.util.ArrayList;

public class FollowActivity extends AppCompatActivity {
    Toolbar toolbar;
    Context context;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        toolbar = (Toolbar) findViewById(R.id.toolbar_follow);
        rv= (RecyclerView) findViewById(R.id.recyclerlist_follow);
        toolbar.setTitle("跟帖");
        setSupportActionBar(toolbar);
        context = FollowActivity.this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final ArrayList<Comments> comments = new ArrayList<>();
        final MyCommentRcy adapter = new MyCommentRcy(comments, context);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(adapter);
        new Thread() {
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocalCommentSQL commentSQLiteOP = new LocalCommentSQL(context, LocalCommentSQL.DATABASE_NAME, null, LocalCommentSQL.DATABASE_VERSION);
                        final ArrayList<Comments> commentses = LocaCommentDBM.query(commentSQLiteOP);
                        adapter.setNewses(commentses);
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
