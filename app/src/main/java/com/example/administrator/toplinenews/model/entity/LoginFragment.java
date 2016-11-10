package com.example.administrator.toplinenews.model.entity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.Const;
import com.example.administrator.toplinenews.commons.SharedPreferenceUtil;
import com.example.administrator.toplinenews.commons.SystemUtils;
import com.example.administrator.toplinenews.commons.URLErrorException;
import com.example.administrator.toplinenews.commons.UrlComposeUtil;
import com.example.administrator.toplinenews.model.biz.parser.UserManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class LoginFragment extends Fragment {

    EditText username;
    EditText password;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, null);
        view.findViewById(R.id.button_register).setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       new RegisterFragment().show(getFragmentManager(),null);

                    }
                });
        view.findViewById(R.id.button_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForgetPassw().show(getFragmentManager(),null);
            }
        });
         username = (EditText) view.findViewById(R.id.editTextname);
         password = (EditText) view.findViewById(R.id.editText_password);

        view.findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernm =username.getText().toString();
                String pwd = password.getText().toString();
                login(
                        usernm,
                        pwd
                );
            }
        });
        return view;
    }

    private void login(String usernm, String pwd) {
        Map<String, String> p = new HashMap<>();
        //TODO 对用户名，密码，邮箱进行本地校验
//        * http://118.244.212.82:9094//newsClient/login?uid=admin&pwd=admin&
//        * imei=abc&ver=1&device=1
        p.put("uid", usernm);
        p.put("pwd", pwd);
        p.put("imei", SystemUtils.getIMEI(getContext()));
        p.put("ver", CommonUtil.getVersionCode(getContext()) + "");
        p.put("device", Const.PHONE);
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_LOGIN, p);
        new LoginTask(usernm,pwd).execute(urlPath);
    }


    class LoginTask extends AsyncTask<String, Void, String> {
        String usernm;
        String pwd;
        public LoginTask(String usernm, String pwd) {
            this.usernm=usernm;
            this.pwd=pwd;
        }

        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return m.register(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "服务器访问失败", Toast.LENGTH_SHORT).show();
            } catch (URLErrorException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "参数有误", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            BaseEntity entity = parseUser(s);
            String status = entity.getStatus();
            if ("0".equals(status)) {
                Register registerInfo = (Register) entity.getData();

                if (registerInfo.getResult().equals("0")) {
                    //登入成功
                    SharedPreferenceUtil.saveToken(getContext(),registerInfo.getToken());
                    SharedPreferenceUtil.saveAccount(getContext(),usernm,pwd);
                    getFragmentManager().beginTransaction().replace(R.id.container_login,
                            new UserInfoFragment()
                    ).commit();
                } else {
                    //失败
                }
                Toast.makeText(getContext(), registerInfo.getExplain(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), entity.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public BaseEntity parseUser(String jsonString) {
        Gson g = new Gson();
        Type t = new TypeToken<BaseEntity<Register>>() {
        }.getType();
        return g.fromJson(jsonString, t);
    }

}
