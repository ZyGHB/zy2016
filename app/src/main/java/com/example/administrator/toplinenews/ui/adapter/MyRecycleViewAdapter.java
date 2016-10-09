package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.ActivityShow;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHold> {

    public void setNewses(ArrayList<News> newses) {
        this.newses = newses;
    }
    private ArrayList<News> newses;

    public ArrayList<News> getNewses() {
        return newses;
    }

    private final Context context;

    public MyRecycleViewAdapter(ArrayList<News> newses, Context context) {
        this.newses = newses;
        this.context = context;
    }

    @Override
    public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_news, null);
        return new MyViewHold(view);
    }

    @Override
    public int getItemCount() {
        return newses==null?0:newses.size();
    }

    @Override
    public void onBindViewHolder(final MyViewHold holder, final int position) {
        final News news = newses.get(position);
        String iconPath = news.getIcon();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (news==null|| TextUtils.isEmpty(news.getLink())) {
                    return;
                }
                Intent intent = new Intent(context, ActivityShow.class);
//                intent.putExtra("url", news.getLink());
//                intent.putExtra("nid", news.getNid());
                intent.putExtra("news", news);
                context.startActivity(intent);
            }
        });
        //异步下载网络图片
        Glide.with(context)
                .load(iconPath)//可以加载本地，也可以下载网络
                .centerCrop()//对bitmap像素缩放
                .placeholder(R.drawable.cccc)//默认图片
                .crossFade()//动画效果
                .into(holder.imageView);//把下载的图片放到imageview中

        holder.tv1.setText(news.getTitle());
        holder.tv2.setText(news.getSummary());
    }

    class MyViewHold extends RecyclerView.ViewHolder {

        TextView tv1;
        TextView tv2;
        ImageView imageView;

        public MyViewHold(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
            tv1 = (TextView) itemView.findViewById(R.id.textView1);
            tv2 = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}

