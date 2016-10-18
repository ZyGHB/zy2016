package com.example.administrator.toplinenews.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.CommonUtil;
import com.example.administrator.toplinenews.common.Const;
import com.example.administrator.toplinenews.common.OkHttpUtil;
import com.example.administrator.toplinenews.common.SharedPreferenceUtil;
import com.example.administrator.toplinenews.common.SystemUtils;
import com.example.administrator.toplinenews.common.URLErrorException;
import com.example.administrator.toplinenews.common.UrlComposeUtil;
import com.example.administrator.toplinenews.model.biz.parser.UserManager;
import com.example.administrator.toplinenews.model.dao.NewsDBManager;
import com.example.administrator.toplinenews.model.entity.BaseEntity;
import com.example.administrator.toplinenews.model.entity.DummyFragment;
import com.example.administrator.toplinenews.model.entity.NewsGroup;
import com.example.administrator.toplinenews.model.entity.User;
import com.example.administrator.toplinenews.ui.BigMap;
import com.example.administrator.toplinenews.ui.CollectActivity;
import com.example.administrator.toplinenews.ui.FollowActivity;
import com.example.administrator.toplinenews.ui.base.LoginActivity;
import com.example.administrator.toplinenews.ui.base.MyBaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class ActivityMain  extends MyBaseActivity {

    private final String TAG="AcitvityMain";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    public  static NavigationView mNavigationView;
    FrameLayout mContentFrame;
    TabLayout tabLayout;
    private static final String PREFERENCES_FILE = "mymaterialapp_settings";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private int mCurrentSelectedPosition;
    private NewsDBManager dbManager;
    ViewPagerAdapter adapter;
    CircleImageView header;
    TextView use_name;
    private List<String> titles = new ArrayList<>();
    private List<Integer> subids = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        //listView = (ListView) findViewById(R.id.listview);

        setUpToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);

        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        setUpNavDrawer();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
       /* header= (CircleImageView) mNavigationView.getHeaderView(1);
        use_name= (TextView) mNavigationView.getHeaderView(2);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mNavigationView);
                openActivity(LoginActivity.class);
            }
        });
*/
        View view = View.inflate(this,R.layout.drawer_header,null);
         header = (CircleImageView) view.findViewById(R.id.iv_header_img);
         use_name= (TextView) view.findViewById(R.id.use_name);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mNavigationView);
                    Bundle bundle=new Bundle();
                String[] account = SharedPreferenceUtil.getAccount(ActivityMain.this);
                  //  Toast.makeText(ActivityMain.this,account[0],Toast.LENGTH_LONG).show();
                if( account[0]==null||account[1]==null) {
                    bundle.putString("key","2");
                    openActivity(LoginActivity.class, bundle);
                }
                else
                {
                    bundle.putString("key","1");
                    openActivity(LoginActivity.class,bundle);
                }
            }
        });
        mNavigationView.addHeaderView(view);
       // mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_item_1:
                       openActivity(CollectActivity.class);
                        break;
                    case R.id.navigation_item_2:
                        openActivity(BigMap.class);
                        break;
                    case R.id.navigation_item_3:
                        openActivity(ActivityMain.class);
                        break;
                    case R.id.navigation_item_4:
                        openActivity(FollowActivity.class);
                        break;
                    default:
                        return true;
                }
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });


        //ViewPager

        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                parseNewsGroupJsonString();
                ActivityMain.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ViewPagerAdapter(getSupportFragmentManager());
                        for(int i=0;i<titles.size();i++)
                        {
                            adapter.addFrag(new DummyFragment(getResources().getColor(R.color.accent_material_light)), titles.get(i));

                        }
                        viewPager.setAdapter(adapter);

                        tabLayout.setupWithViewPager(viewPager);
                        DummyFragment fragment = (DummyFragment) adapter.getmFragmentList().get(0);
                        String url = "http://118.244.212.82:9092/newsClient/"+"news_list?ver=1&subid="+subids.get(0) +"&dir=1&nid=1&stamp=20140321&cnt=20";
                        fragment.myAsyncLoad(url);
                        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                super.onTabSelected(tab);
                                //viewpager滑动和tablayout的点击同时触发这个方法一次
                                //Toast.makeText(getApplicationContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                                //让选中的fragment加载新闻数据
                                int position = tab.getPosition();
                                DummyFragment fragment = (DummyFragment) adapter.getmFragmentList().get(position);
//                              "ver=1&subid="+subids.get(position) +"&dir=1&nid=1&stamp=20140321&cnt=20"
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("ver", "1");
                                map.put("subid", subids.get(position)+"");
                                map.put("dir", "1");
                                map.put("nid", "1");
                                map.put("stamp", "20140321");
                                map.put("cnt", "20");
                                String url = UrlComposeUtil.getUrlPath( Const.URL_NEW_LIST,map);
                                fragment.myAsyncLoad(url);
                            }
                        });
                    }
                });
            }
        }.start();


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        ImageView header = (ImageView) findViewById(R.id.header);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });



    }
        public static  void setmune()
        {
            mNavigationView.getMenu().getItem(0).setChecked(true);
        }

    public void parseNewsGroupJsonString() {
        String url = "http://118.244.212.82:9092/newsClient/news_sort?ver=1&imei=1";
        //String data = HttpUtil.getJsonString(url);
        String data = OkHttpUtil.getString(url);
        if (data == null) {
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "onCreate: " + data);

        Gson gson = new Gson();
        Type type = new TypeToken<NewsGroup<List<NewsGroup.DataBean<List<NewsGroup.DataBean.SubgrpBean>>>>>() {
        }.getType();

        NewsGroup newsGroup = gson.fromJson(data, type);
        Log.d(TAG, "parseNewsGroupJsonString: " + newsGroup.getMessage());
        List<NewsGroup.DataBean> data1 = (List<NewsGroup.DataBean>) newsGroup.getData();
        for (NewsGroup.DataBean dataBean : data1) {
            String group = dataBean.getGroup();
            Log.d(TAG, "group: " + group);
            List<NewsGroup.DataBean.SubgrpBean> subgrp = (List<NewsGroup.DataBean.SubgrpBean>) dataBean.getSubgrp();
            for (NewsGroup.DataBean.SubgrpBean subgrpBean : subgrp) {
                Log.d(TAG, "sub grp: "+subgrpBean.getSubid());
                titles.add(subgrpBean.getSubgroup());
                subids.add(subgrpBean.getSubid());
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
        Menu menu = mNavigationView.getMenu();
        menu.getItem(mCurrentSelectedPosition).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("今日头条");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_drawer);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mUserLearnedDrawer = true;
            saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
        }

    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferenceUtil.getAccount(this)[0] != null) {
            //加载用户信息
            loadUserInfo();
        }

    }

    public void loadUserInfo() {
        if (SharedPreferenceUtil.getAccount(this)[0] == null) {
            //用户名为空就设置默认图片头像
            use_name.setText("登入");
            header.setImageResource(R.mipmap.biz_pc_main_info_profile_avatar_bg_dark);
            return;
        }
        Map<String, String> p = new HashMap<>();
        //user_home?ver=版本号&imei=手机标识符&token =用户令牌
        p.put("ver", CommonUtil.getVersionCode(this) + "");
        p.put("imei", SystemUtils.getIMEI(this));
        p.put("token", SharedPreferenceUtil.getToken(this));
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_USER_INFOR, p);
        new LoadUserTask().execute(urlPath);
    }

    class LoadUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return m.getUserInfo(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ActivityMain.this, "服务器访问失败", Toast.LENGTH_SHORT).show();
            } catch (URLErrorException e) {
                e.printStackTrace();
                Toast.makeText(ActivityMain.this, "参数有误", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析返回字符串
            BaseEntity baseEntity = parseUserInfo(s);
            if (baseEntity == null) {
                return;
            }
            if (baseEntity.getStatus().equals("0")) {
                //成功 ，把数据设置到view中
                User user = (User) baseEntity.getData();
                String portrait = user.getPortrait();
                Glide.with(ActivityMain.this).load(portrait)
                        .centerCrop().into(header);
                use_name.setText(user.getUid());
            } else {
                //失败
                Toast.makeText(ActivityMain.this, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private BaseEntity parseUserInfo(String s) {
        Gson g = new Gson();
        Type t = new TypeToken<BaseEntity<User>>() {
        }.getType();
        return g.fromJson(s, t);
    }
}
