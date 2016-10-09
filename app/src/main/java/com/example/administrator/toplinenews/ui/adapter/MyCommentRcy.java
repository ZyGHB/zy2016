package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.entity.Comments;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/29 0029.
 */

public class MyCommentRcy extends RecyclerView.Adapter<MyCommentRcy.MyViewHold> {

    public void setNewses(ArrayList<Comments> newses) {
        this.newses = newses;
    }
    private ArrayList<Comments> newses;

    public ArrayList<Comments> getNewses() {
        return newses;
    }

    private final Context context;

    public MyCommentRcy(ArrayList<Comments> newses, Context context) {
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
        final Comments news = newses.get(position);
        String iconPath = news.getPortrait();
        //异步下载网络图片
        Glide.with(context)
                .load(iconPath)//可以加载本地，也可以下载网络
                .centerCrop()//对bitmap像素缩放
                .placeholder(R.drawable.cccc)//默认图片
                .crossFade()//动画效果
                .into(holder.imageView);//把下载的图片放到imageview中

        holder.name.setText(news.getUid());
        holder.content.setText(news.getContent());
        holder.time.setText(news.getStamp());
    }

    class MyViewHold extends RecyclerView.ViewHolder {

        TextView content;
        TextView name;
        ImageView imageView;
        TextView time;

        public MyViewHold(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
            content = (TextView) itemView.findViewById(R.id.textView1);
            name = (TextView) itemView.findViewById(R.id.textView2);
            time= (TextView) itemView.findViewById(R.id.text_time);
        }
    }
}