package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.LoadImage;
import com.example.administrator.toplinenews.commons.LogUtil;
import com.example.administrator.toplinenews.model.entity.News;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class NewsAdaper extends MyBaseAdapter<News> {
    /**
     * 加载图片之前默认图片
     */
    private Bitmap defaultBitmap;
    private ListView listview;
    private LoadImage loadImage;



    public NewsAdaper(Context c, ListView lv) {
        super(c);
        defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.cccc);
        listview = lv;
        loadImage=new LoadImage(c, listener);
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
                    ImageView iv=(ImageView) listview.findViewWithTag(url);
                    LogUtil.d(url);
                    if(iv!=null){
                        LogUtil.d(" 异步加载得到图片的 url="+url);
                        iv.setImageBitmap(bitmap);
                    }
                }
            };

    /**
     * 该方法的返回值 bitmap 对象最终会返回给 onPostExecute()，如果想要在请求数
     据 的 过 程 中 ， 更 新 进 度 条 ， 则 需 要 在 doInBackground() 中 手 动 调 用
     publishProgress(intvalues)来更新。
     *
     */

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_news, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        News news = myList.get(position);
        holdView.tv_title.setText(news.getTitle());
        holdView.tv_text.setText(news.getSummary());
        //Toast.makeText(context,news.getTitle()+"",Toast.LENGTH_LONG).show();
        holdView.iv_list_image.setImageBitmap(defaultBitmap);// 默认图片
        //1 ，得到图片的地址
        String uriImage=news.getIcon();
        String newuriImage = uriImage.replaceAll("localhost", "118.244.212.82");
        LogUtil.d(newuriImage);
        // 给每个图片控件存入编号把图片的名字作为表示
        holdView.iv_list_image.setTag(CommonUtil.NETPATH+newuriImage);
          // 获取图片 1.  先从网络 2. 如果已经下载过了存在本地文件中下次启动程序从文件读取 3. 当程序不关闭再次进入该界面从内存中读取
        Bitmap bitmap=loadImage.getBitmap(newuriImage);
         // 如果不是网络的则马上加载可以是文件或内存
        if(bitmap!=null){
            holdView.iv_list_image.setImageBitmap(bitmap);
          }
        return convertView;
    }

    private class HoldView {
        public ImageView iv_list_image;
        public TextView tv_title;
        public TextView tv_text;
        public HoldView(View view){
            this.iv_list_image=(ImageView)
                    view.findViewById(R.id.imageView1);
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_text = (TextView) view.findViewById(R.id.textView2);
        }
    }
}
