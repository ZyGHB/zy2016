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
import com.example.administrator.toplinenews.model.entity.BigmapUit;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class BigmapAdapter extends RecyclerView.Adapter<BigmapAdapter.VersionViewHolder> {
    Context context;

    public List<BigmapUit> getBigmaps() {
        return bigmaps;
    }

    List<BigmapUit> bigmaps;

    public void setBigmaps(List<BigmapUit> bigmaps) {
        this.bigmaps = bigmaps;
    }

    public BigmapAdapter(Context context, List<BigmapUit> bigmaps) {
        this.context=context;
        this.bigmaps=bigmaps;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.bigmaplist, null);
        VersionViewHolder viewHolder = new VersionViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder holder, int position) {
        BigmapUit bigmap = bigmaps.get(position);
        Glide.with(context).load(bigmap.getImage())
                .centerCrop().into(holder.iv_list_image);
        holder.tv_title.setText(bigmap.getIntroduct());

    }

    @Override
    public int getItemCount() {
        return bigmaps==null?0:bigmaps.size();
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_list_image;
        public TextView tv_title;


        public VersionViewHolder(View itemView) {
            super(itemView);

            iv_list_image = (ImageView) itemView.findViewById(R.id.image_bigmap);
            tv_title = (TextView) itemView.findViewById(R.id.text_bigmap);

        }
    }
}