package com.guang.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.model.UserAccount;
import com.guang.app.util.FileUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaoguang on 2017/2/13.
 */
//@ContentView(R.layout.login)
public class LoginActivity extends BaseActivity {
//    @Bind(R.id.btn_login) Button btn_login;
    @Bind(R.id.ed_username) EditText edSno;
    @Bind(R.id.ed_password) EditText edpwd;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login) void login() {
        String sno = edSno.getText().toString().trim();
        String pwd = edpwd.getText().toString().trim();

        AppConfig.sno = sno;
        AppConfig.idsPwd = AppConfig.jwPwd = pwd;
        FileUtils.setStoredAccount(this,new UserAccount(sno,pwd,pwd));
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
