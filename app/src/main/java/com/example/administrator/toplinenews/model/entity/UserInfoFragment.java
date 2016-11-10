package com.example.administrator.toplinenews.model.entity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.Const;
import com.example.administrator.toplinenews.commons.GetPictureUtil;
import com.example.administrator.toplinenews.commons.OkHttpUtil;
import com.example.administrator.toplinenews.commons.SharedPreferenceUtil;
import com.example.administrator.toplinenews.commons.SystemUtils;
import com.example.administrator.toplinenews.commons.URLErrorException;
import com.example.administrator.toplinenews.commons.UrlComposeUtil;
import com.example.administrator.toplinenews.model.biz.parser.UserManager;
import com.example.administrator.toplinenews.ui.adapter.ActivityMain;
import com.example.administrator.toplinenews.ui.adapter.LoginAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class UserInfoFragment extends Fragment {
    Context context;
    TextView usename;
    TextView integral;
    TextView comment_count;
    Button btn_exit;
    CircleImageView icon;
    ListView list;
    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, null);
        usename = (TextView) view.findViewById(R.id.name);
        integral = (TextView) view.findViewById(R.id.integral);
        comment_count = (TextView) view.findViewById(R.id.comment_count);
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil.saveAccount(context,null,null);
                Intent intent=new Intent(context,ActivityMain.class);
                startActivity(intent);
            }
        });



         toolbar = (Toolbar) view.findViewById(R.id.toolbar_userinfo);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ActivityMain.class);
                startActivity(intent);
            }
        });

        icon = (CircleImageView) view.findViewById(R.id.icon);
        list= (ListView) view.findViewById(R.id.list);
        context = getContext();
        loadUserInfo();
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpupo();
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null) {
            return;
        }
        GetPictureUtil.onActivityResult(requestCode, data, this);
        if (resultCode == Activity.RESULT_OK) {
            String path = GetPictureUtil.getFilePathString(getActivity());
//            if (requestCode==REQUEST_PICK) {
//                Uri data1 = data.getData();
//                path = data1.getPath();
//            } else if (requestCode == REQUEST_CAMERA) {
//                Bundle extras = data.getExtras();
//            }
            Glide.with(context)
                    .load(path)//可以加载本地，也可以下载网络
                    .centerCrop()//对bitmap像素缩放
                    .placeholder(R.drawable.cccc)//默认图片
                    .crossFade()//动画效果
                    .into(icon);//把下载的图片放到imageview中
            //上传图片到服务器 user_image?token=用户令牌& portrait =头像
            File file = new File(path);
            new UploadFileTask(file).execute(Const.URL_USER_IMAGE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //当activity创建的时候，回调
        if (getActivity() instanceof ActivityMain) {
            final ActivityMain activity = (ActivityMain) getActivity();
            /*activity.setSupportActionBar(toolbar);
            activity.backToMainActivity(toolbar);*/
            activity.loadUserInfo();
        }
    }

   public void loadUserInfo() {
        Map<String, String> p = new HashMap<>();
        //user_home?ver=版本号&imei=手机标识符&token =用户令牌
        p.put("ver", CommonUtil.getVersionCode(context) + "");
        p.put("imei", SystemUtils.getIMEI(context));
        p.put("token", SharedPreferenceUtil.getToken(context));
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_USER_INFOR, p);
        new LoadUserTask().execute(urlPath);
    }

    class UploadFileTask extends AsyncTask<String, Void, String> {

        private File file;

        public UploadFileTask(File file) {

            this.file = file;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return OkHttpUtil.postFile(params[0], file, SharedPreferenceUtil.getToken(getContext()));
                //Caused by: android.os.NetworkOnMainThreadException
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null&&s.contains("OK"))
            {
                loadUserInfo();
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
    class LoadUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return  m.getUserInfo(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "服务器访问失败", Toast.LENGTH_SHORT).show();
            } catch (URLErrorException e) {
                e.printStackTrace();
                Toast.makeText(context, "参数有误", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //解析返回字符串
            BaseEntity baseEntity = parseUserInfo(s);
            Log.d("UserInfoFragment",baseEntity.getStatus());
            if (baseEntity == null) {
                Toast.makeText(context,"123",Toast.LENGTH_LONG).show();
                return;
            }
            if (baseEntity.getStatus().equals("0")) {
                //成功 ，把数据设置到view中
                setDataToView(baseEntity);

            } else {
                //失败
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void setDataToView(BaseEntity baseEntity) {
        User user = (User) baseEntity.getData();
        String portrait = user.getPortrait();
        Toast.makeText(context, "12"+user.getUid(), Toast.LENGTH_SHORT).show();
        usename.setText(user.getUid());
        Glide.with(this).load(portrait)
                .centerCrop().into(icon);
        List<LoginLog> loginlog = user.getLoginlog();
        LoginAdapter adapter=new LoginAdapter(context);
        adapter.appendData(loginlog,true);
        list.setAdapter(adapter);
    }

    private BaseEntity parseUserInfo(String s) {
        Gson g = new Gson();
        Type t = new TypeToken<BaseEntity<User>>() {
        }.getType();
        return g.fromJson(s, t);
    }



    private void showpupo() {
        final PopupWindow window = new PopupWindow(context);
        window.setWidth(context.getResources().getDisplayMetrics().widthPixels);
        window.setHeight(600);
        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_custom_alert, null);
        window.setContentView(view1);
        view1.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        view1.findViewById(R.id.photo_library).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPictureUtil.openGallery(UserInfoFragment.this);
                window.dismiss();
            }
        });
        view1.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPictureUtil.openCamera(UserInfoFragment.this);

                window.dismiss();
            }
        });
//                //要取消泡泡窗口,必须设置背景图片
//                window.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_custom_dialog_background));
        //指定泡泡窗口显示的位置
        window.showAsDropDown(
                btn_exit,//参照对象
                0,//x轴偏移值
                -300//y轴偏移值
        );
    }



}