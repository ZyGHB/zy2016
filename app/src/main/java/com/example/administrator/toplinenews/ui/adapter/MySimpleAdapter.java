package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.LoadImage;
import com.example.administrator.toplinenews.commons.LogUtil;
import com.example.administrator.toplinenews.model.entity.News;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/13 0013.
 */
public class MySimpleAdapter extends RecyclerView.Adapter <MySimpleAdapter.VersionViewHolder> implements View.OnClickListener {
    protected ArrayList<News> list ;
    private Bitmap defaultBitmap;
    RecyclerView recyclerView;
    private LoadImage loadImage;
    OnItemClickListener clickListener=null;

    public void setNewses(ArrayList<News> newses) {
        this.list = newses;
    }
    public MySimpleAdapter(Context c, RecyclerView lv)
    {
          super();
        defaultBitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.cccc);
        recyclerView = lv;
        loadImage=new LoadImage(c, listener);
    }


    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item, parent, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    private LoadImage.ImageLoadListener listener=new
            LoadImage.ImageLoadListener() {
                /**
                 * 回调方法
                 *@parambitmap  请求回来的 bitmap
                 *@paramurl  图片请求地址
                 */
                public void imageLoadOk(Bitmap bitmap, String url) {
                    // 类似于 findviewById  得到每个 listview  的图片通过异步加载显示图片
                    ImageView iv=(ImageView) recyclerView.findViewWithTag(url);
                    LogUtil.d(url);
                    if(iv!=null){
                        LogUtil.d(" 异步加载得到图片的 url="+url);
                        iv.setImageBitmap(bitmap);
                    }
                }
            };

    @Override
    public void onBindViewHolder(VersionViewHolder holder, int position) {
        News news = list.get(position);
        holder.tv_text.setText(news.getSummary());
        holder.tv_title.setText(news.getTitle());
        holder.iv_list_image.setImageBitmap(defaultBitmap);// 默认图片
        //1 ，得到图片的地址
        String uriImage=news.getIcon();
        String newuriImage = uriImage.replaceAll("localhost", "118.244.212.82");
        LogUtil.d(newuriImage);
        // 给每个图片控件存入编号把图片的名字作为表示
        holder.iv_list_image.setTag(CommonUtil.NETPATH+newuriImage);
        // 获取图片 1.  先从网络 2. 如果已经下载过了存在本地文件中下次启动程序从文件读取 3. 当程序不关闭再次进入该界面从内存中读取
        Bitmap bitmap=loadImage.getBitmap(newuriImage);
        // 如果不是网络的则马上加载可以是文件或内存
        if(bitmap!=null){
            holder.iv_list_image.setImageBitmap(bitmap);
        }
        holder.itemView.setTag(list.get(position));

    }


    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            //注意这里使用getTag方法获取数据
            clickListener.onItemClick(v,(News)v.getTag());
        }
    }

    class VersionViewHolder extends RecyclerView.ViewHolder  {
        CardView cardItemLayout;
        public ImageView iv_list_image;
        public TextView tv_title;
        public TextView tv_text;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            iv_list_image= (ImageView) itemView.findViewById(R.id.imageView_listitem);
            tv_title = (TextView) itemView.findViewById(R.id.listitem_name);
            tv_text = (TextView) itemView.findViewById(R.id.listitem_subname);



            /*if (isHomeList) {
                itemView.setOnClickListener(this);
            } else {
                subTitle.setVisibility(View.GONE);
            }
*/
        }


    }
    public void addendData(ArrayList alist){

        this.list=alist;
    }
    public void update(){
        // 刷新适配器
        this.notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        public void onItemClick(View view,  News news);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
            this.clickListener = listener;
        }

}