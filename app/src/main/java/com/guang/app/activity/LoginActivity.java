package com.guang.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.fragment.MeFragment;
import com.guang.app.model.BasicInfo;
import com.guang.app.model.UserAccount;
import com.guang.app.util.FileUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    String sno,pwd;
    @OnClick(R.id.btn_login) void login() {
        final String sno = edSno.getText().toString().trim();
        final String pwd = edpwd.getText().toString().trim();
//        AppConfig.sno = sno;
//        AppConfig.idsPwd = AppConfig.jwPwd = pwd;
//        FileUtils.setStoredAccount(this,new UserAccount(sno,pwd,pwd));
//        startActivity(new Intent(this, MainActivity.class));
//        this.finish();

//        final String sno = edSno.getText().toString().trim();
//        final String pwd = edpwd.getText().toString().trim();
        //拦截的请求会带AppConfig的账号密码，但没登陆的时候又不会去设置这个值
//        AppConfig.sno = sno;AppConfig.idsPwd = AppConfig.jwPwd = pwd;
        //故设置之，Activity检测则也加上本地文件判断，不能只根据这个属性
//        LogUtils.e(AppConfig.sno);
        AppConfig.sno = sno;
        AppConfig.idsPwd = AppConfig.jwPwd = pwd;

        MeFragment.factory.getBasicInfo(new Observer<BasicInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(BasicInfo value) {
                value.setId(MeFragment.localId);
                value.save();

                FileUtils.setStoredAccount(LoginActivity.this,new UserAccount(sno,pwd,pwd));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {

            }
        });

    }


}
