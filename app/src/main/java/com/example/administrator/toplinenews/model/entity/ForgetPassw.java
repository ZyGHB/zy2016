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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.common.CommonUtil;
import com.example.administrator.toplinenews.common.Const;
import com.example.administrator.toplinenews.common.URLErrorException;
import com.example.administrator.toplinenews.common.UrlComposeUtil;
import com.example.administrator.toplinenews.model.biz.parser.ParserUser;
import com.example.administrator.toplinenews.model.biz.parser.UserManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19 0019.
 */
public class ForgetPassw extends DialogFragment {
    TextView textemail;
    Button button_findpassw;
    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.forgetpassw, null);
        button_findpassw = (Button) view.findViewById(R.id.button_findpassw);
        textemail = (TextView) view.findViewById(R.id.textemail);
        context = getContext();
        button_findpassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwd(textemail.getText().toString().trim());
            }
        });
        builder.setView(view);
        return builder.create();
    }

    private void findPwd(String email) {
        //?ver=" + args[0] + "&email=" + args[1]
        //TODO 校验邮箱的格式是否符合规则

        Map<String, String> map = new HashMap<>();
        map.put("ver", CommonUtil.getVersionCode(context) + "");
        map.put("email", email);
        String urlPath = UrlComposeUtil.getUrlPath(Const.URL_FORGET_PWD, map);

        //异步任务请求网络。。。
        new FindTask().execute(urlPath, "ttt");
    }

    class FindTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                //获取参数
                String url = params[0];
                //String ttt = params[1];
                UserManager userManager = new UserManager();
                return userManager.forgetPwd(url);
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
            if (s != null) {
                //对json字符串进行解析
                //{"message":"OK","status":0,"data":{"result":0,"explain":"已成功发送到邮箱"}}
                BaseEntity baseEntity = ParserUser.parserRegister(s);
                Register data = (Register) baseEntity.getData();
                if ("0".equals(baseEntity.getStatus())) {
                    //成功找回
                } else {
                    //失败
                }
                Toast.makeText(context, data.getExplain(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "找回密码失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}