package com.example.administrator.toplinenews.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.Const;
import com.example.administrator.toplinenews.commons.OkHttpUtil;
import com.example.administrator.toplinenews.commons.SharedPreferenceUtil;
import com.example.administrator.toplinenews.commons.UrlComposeUtil;
import com.example.administrator.toplinenews.model.dao.LocaCommentDBM;
import com.example.administrator.toplinenews.model.dao.LocalCommentSQL;
import com.example.administrator.toplinenews.model.dao.LocalNewsDBManager;
import com.example.administrator.toplinenews.model.dao.LocalNewsSQLiteOP;
import com.example.administrator.toplinenews.model.entity.BaseEntity;
import com.example.administrator.toplinenews.model.entity.Comments;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/9/5 0005.
 */
public class ActivityShow extends MyBaseActivity {
    private ProgressBar progressBar;
    /** 网页*/
    private WebView webView;
    /** 当前的新闻*/
    private News news;
    /** 返回按钮*/
    private ImageButton imgRtn;
    EditText  et_comment;
    private MenuItem item;
    String urlPath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ShareSDK.initSDK(this);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        et_comment = (EditText) findViewById(R.id.et_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置返回箭头可用
        //给图片设置的点击事件
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView=(WebView) findViewById(R.id.webView1);
       // imgRtn = (ImageButton) findViewById(R.id.title_bar);
        /*imgRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        news=(News) getIntent().getSerializableExtra("news");
        // 设置 webview  的属性缓存模式离线
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置加载全部后的监听
        webView.setWebViewClient(viewclient);
        // 设置当加载时的监听
        webView.setWebChromeClient(chromeClient);
        // 设置路径
        webView.loadUrl(news.getLink());


        final Intent intent = getIntent();

        if (intent != null) {
            news = (News) intent.getSerializableExtra("news");
            String url = news.getLink();
            //设置webview的客户端,
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            webView.loadUrl(url);
        }
        final int nid = news.getNid();
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment(nid);
            }
        });
        //加载当前新闻评论的数量
        loadCommentCount(nid);
        Log.d("ActivityShow", news.getLink());
        Toast.makeText(ActivityShow.this,  news.getLink(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_comment_count,menu);
        item = menu.findItem(R.id.menu_comment_count);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_comment_count) {
            //跳到展示评论页面
            Intent intent = new Intent(ActivityShow.this, ShowCommentActivity.class);
            intent.putExtra("news", news);
            startActivity(intent);
        }
        else if (itemId == R.id.menu_favorite) {
            //本地收藏，存到sqlite
            //LocalNewsSQLiteOP localNewsSQLiteOP = application.localNewsSQLiteOP;
            LocalNewsSQLiteOP  localNewsSQLiteOP = new LocalNewsSQLiteOP(
                    this,
                    LocalNewsSQLiteOP.DATABASE_NAME,
                    null,
                    LocalNewsSQLiteOP.DATABASE_VERSION
            );
            String msg;
            if (LocalNewsDBManager.isExistNews(localNewsSQLiteOP, news.getNid())) {
                msg = "已收藏";
            } else {
                long insert = LocalNewsDBManager.insert(localNewsSQLiteOP, news);
                if (insert > 0) {
                    msg = "收藏成功";
                } else {
                    msg  = "收藏失败";
                }
            }
            Toast.makeText(ActivityShow.this,msg , Toast.LENGTH_SHORT).show();
        }else if (itemId == R.id.menu_share) {
            //TODO 分享
            showShare();
        }
        return super.onOptionsItemSelected(item);
    }

 //获取评论数量
    private void loadCommentCount(int nid) {
        //cmt_num?ver=版本号& nid=新闻编号
        Map<String, String> p = new HashMap<>();
        p.put("ver", CommonUtil.getVersionCode(this) + "");
        p.put("nid", nid+"");
        urlPath = UrlComposeUtil.getUrlPath(Const.URL_USER_COMMENT_COUNT, p);
        Log.d("ActivityShow",urlPath);
        new LoadCommentCountTask().execute(urlPath);
    }

    class LoadCommentCountTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            return OkHttpUtil.getString(path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析json字符串
            Gson gson = new Gson();
            BaseEntity entity = gson.fromJson(s, BaseEntity.class);
            if (entity == null) {
                Toast.makeText(ActivityShow.this, "网络异常", Toast.LENGTH_SHORT).show();
            } else {
                double count = (double) entity.getData();//评论数量
                item.setTitle("评论数"+(int)count);
            }

        }
    }
    //发送评论
    private void sendComment(int nid) {

        String content = et_comment.getText().toString().trim();
        //cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容

        Map<String, String> p = new HashMap<>();
        p.put("ver", CommonUtil.getVersionCode(this) + "");
        p.put("nid", nid+"");
        p.put("token", SharedPreferenceUtil.getToken(this));
        p.put("imei", "8989"/*SystemUtils.getIMEI(this)*/);
        p.put("ctx", content);

        //发送评论给服务器,需要异步请求
        new UpLoadCommentTask().execute(p);
    }

    class UpLoadCommentTask extends AsyncTask< Map<String, String>, Void, String> {

        @Override
        protected String doInBackground(Map<String, String>... params) {
            try {
                return OkHttpUtil.postString(Const.URL_USER_COMMENT, params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Comments comments=new Comments();
                comments.setPortrait("file:///assets/a7.jpg");
                comments.setUid("zy");
                comments.setStamp(CommonUtil.getDate());
                comments.setContent("6666666");
                LocalCommentSQL commentSQLiteOP = new LocalCommentSQL(
                        ActivityShow.this,
                        LocalCommentSQL.DATABASE_NAME,
                        null,
                        LocalCommentSQL.DATABASE_VERSION
                );
                LocaCommentDBM.insert(commentSQLiteOP,comments);
                webView.requestFocus();//获取焦点
                et_comment.setText("");//清空评论
                Toast.makeText(ActivityShow.this, s, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ActivityShow.this, s, Toast.LENGTH_SHORT).show();
            }
            CommonUtil.hideKeyBoard(ActivityShow.this);
        }
    }
    private WebViewClient viewclient=new WebViewClient(){
        // 在点击请求的是链接是才会调用，重写此方法返回 true  表明点击网页里的 面的链接还是在当前的 webview  里跳转，不跳到浏览器那边。
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            webView.loadUrl(url);
            return true;
        }
    };
    /**
     *  进度条的使用, 根据进度
     */
    private WebChromeClient chromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if(progressBar.getProgress()==100){
                progressBar.setVisibility(View.GONE);
            }
        }
    };



    private void showShare() {

        ShareSDK.initSDK(this);

        OnekeyShare oks = new OnekeyShare();

//关闭sso授权

        oks.disableSSOWhenAuthorize();



// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用

        oks.setTitle(news.getTitle());

// titleUrl是标题的网络链接，QQ和QQ空间等使用

        oks.setTitleUrl(news.getLink());

// text是分享文本，所有平台都需要这个字段

        oks.setText("我是分享文本");

// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片

// url仅在微信（包括好友和朋友圈）中使用

        oks.setUrl(news.getLink());

// comment是我对这条分享的评论，仅在人人网和QQ空间使用

        oks.setComment(news.getSummary());

// site是分享此内容的网站名称，仅在QQ空间使用

        oks.setSite(getString(R.string.app_name));

// siteUrl是分享此内容的网站地址，仅在QQ空间使用

        oks.setSiteUrl(news.getLink());



// 启动分享GUI

        oks.show(this);

    }
}
