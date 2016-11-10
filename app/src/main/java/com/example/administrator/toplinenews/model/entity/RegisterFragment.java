package com.example.administrator.toplinenews.model.entity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.commons.CommonUtil;
import com.example.administrator.toplinenews.commons.Const;
import com.example.administrator.toplinenews.commons.SharedPreferenceUtil;
import com.example.administrator.toplinenews.commons.URLErrorException;
import com.example.administrator.toplinenews.commons.UrlComposeUtil;
import com.example.administrator.toplinenews.model.biz.parser.ParserUser;
import com.example.administrator.toplinenews.model.biz.parser.UserManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class RegisterFragment extends DialogFragment
{
    private static final String TAG = "RegisterFragment";
    private Context context;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_register, null);
        dialog.setView(view);
        final EditText username= (EditText) view.findViewById(R.id.editText_name);
        final EditText email= (EditText) view.findViewById(R.id.editText_email);
        final EditText passw= (EditText) view.findViewById(R.id.editText_psw);
        context = getContext();
        view.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(
                        username.getText().toString(),
                        email.getText().toString(),
                        passw.getText().toString()
                );
            }
        });
        return dialog.create();
    }

    private void register(String username, String email, String password) {
        //TODO 对用户名，密码，邮箱进行本地校验
        Map<String, String> p = new HashMap<>();
        // user_register?ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
        p.put("ver", CommonUtil.getVersionCode(context) + "");
        p.put("uid", username);
        p.put("email", email);
        p.put("pwd", password);
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_REGISTER, p);
        new RegisterTask().execute(urlPath);
    }

    class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            UserManager m = new UserManager();
            try {
                return m.register(params[0]);
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
            BaseEntity<Register> registerInfo = ParserUser.parserRegister(s);
            if (registerInfo != null) {
                if (registerInfo.getData().getResult().equals("0")) {
                    //注册成功,跳到用户信息界面
                    //让dialog消失
                    SharedPreferenceUtil.saveToken(context,registerInfo.getData().getToken());
                    dismiss();
                    //再跳转到用户信息界面
                    getFragmentManager().beginTransaction().replace(R.id.container_login,
                            new UserInfoFragment()
                    ).commit();
                } else {
                    //注册失败
                }
                Toast.makeText(context, registerInfo.getData().getExplain(), Toast.LENGTH_SHORT).show();
            } else {
                //注册失败
                Toast.makeText(context, "failed ...", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
