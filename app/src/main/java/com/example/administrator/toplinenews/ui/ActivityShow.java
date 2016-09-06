package com.example.administrator.toplinenews.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.entity.News;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        webView=(WebView) findViewById(R.id.webView1);
        imgRtn = (ImageButton) findViewById(R.id.title_bar);
        imgRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        news=(News) getIntent().getSerializableExtra("news");
        // 设置 webview  的属性缓存模式离线
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置加载全部后的监听
        webView.setWebViewClient(viewclient);
        // 设置当加载时的监听
        webView.setWebChromeClient(chromeClient);
        // 设置路径
        webView.loadUrl(news.getLink());
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

}
