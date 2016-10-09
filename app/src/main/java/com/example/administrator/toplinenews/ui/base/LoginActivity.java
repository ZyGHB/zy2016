package com.example.administrator.toplinenews.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.toplinenews.R;
import com.example.administrator.toplinenews.model.entity.LoginFragment;
import com.example.administrator.toplinenews.model.entity.UserInfoFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_login);
        Bundle bundle = this.getIntent().getExtras();
        String key = bundle.getString("key");
        Toast.makeText(LoginActivity.this,key,Toast.LENGTH_LONG).show();
        if (key.equals("2"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_login,new LoginFragment()).commit();
        }
       else if(key.equals("1"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_login,new UserInfoFragment()).commit();
        }

    }
}
