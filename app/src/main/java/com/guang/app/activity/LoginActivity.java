package com.guang.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.api.ErrorCode;
import com.guang.app.api.InfoApiFactory;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.FewSztz;
import com.guang.app.model.Schedule;
import com.guang.app.model.UserAccount;
import com.guang.app.util.FileUtils;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
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
    @Bind(R.id.ed_jw_password) EditText edJwPwd;
    @Bind(R.id.cb_login_license) CheckBox cbLicense;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edJwPwd.setVisibility(View.GONE);
    }

    int mJwOk = 0; //教务系统密码是否对
    private final int mJwValueInit = 0;
    private final int mJwValueError = -1;
    private final int mJwValueOk = 1;
    String sno,pwd;
    @OnClick(R.id.btn_login) void login() {
        final String sno = edSno.getText().toString().trim();
        final String pwd = edpwd.getText().toString().trim();
        final String jwPwd = edJwPwd.getText().toString().trim();
        if (TextUtils.isEmpty(sno) || TextUtils.isEmpty(pwd)
                || (edJwPwd.getVisibility() == View.VISIBLE && TextUtils.isEmpty(jwPwd))) {
            Toast.makeText(this, "还没输入完呢", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cbLicense.isChecked()) {
            Toast.makeText(this, "请先勾选下方协议", Toast.LENGTH_SHORT).show();
            return;
        }

//        final String sno = edSno.getText().toString().trim();
//        final String pwd = edpwd.getText().toString().trim();
        //拦截的请求会带AppConfig的账号密码，但没登陆的时候又不会去设置这个值
//        AppConfig.sno = sno;AppConfig.idsPwd = AppConfig.jwPwd = pwd;
        //故设置之，Activity检测则也加上本地文件判断，不能只根据这个属性
//        LogUtils.e(AppConfig.sno);
        AppConfig.sno = sno;
        AppConfig.idsPwd = AppConfig.jwPwd = pwd;   //默认两系统一样
        if (edJwPwd.getVisibility() == View.VISIBLE) {
            AppConfig.jwPwd = jwPwd;
        }
        LogUtils.e(AppConfig.sno + " " + AppConfig.jwPwd + " " + AppConfig.idsPwd);
        mJwOk = mJwValueInit;
        //检测教务系统密码对错
        JwApiFactory jwApiFactory = JwApiFactory.getInstance();
        jwApiFactory.getSchedule("", JwApiFactory.MERGE_SCHEDULE, new Observer<List<Schedule>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<Schedule> value) {
                if (value.size() == 0) {//账号对，但没课表（如没课）
                    return;
                }
                mJwOk = mJwValueOk;
                //教务通
                DataSupport.deleteAll(Schedule.class);
                DataSupport.saveAll(value);
                FileUtils.setStoredAccount(LoginActivity.this, new UserAccount(sno, pwd, pwd));
                MobclickAgent.onProfileSignIn(sno);//友盟统计用户信息
            }

            @Override
            public void onError(Throwable e) {
                if (e.getLocalizedMessage().equals(ErrorCode.pwdError + "")) {
                    LogUtils.e("教务密码错误-根据code判断出");
                    Toast.makeText(LoginActivity.this, "教务系统密码错误，请手动输入", Toast.LENGTH_SHORT).show();
                    mJwOk = mJwValueError;
                    edJwPwd.setVisibility(View.VISIBLE);
                    return;
                }
                LogUtils.e(e.getMessage());
                Toast.makeText(LoginActivity.this, "教务系统" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        });

        InfoApiFactory infoFactory = InfoApiFactory.getInstance();
        infoFactory.getFewSztz(new Observer<List<FewSztz>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<FewSztz> value) {
                if (mJwOk == mJwValueError) { //教务密码错，因为两个请求网络顺序问题不一定错的时候能判断出，但出现这情况肯定教务密码错
                    LogUtils.e("教务密码错，信息门户对");
                    return;
                }
                FileUtils.setStoredAccount(LoginActivity.this, new UserAccount(sno, AppConfig.idsPwd, AppConfig.jwPwd));
                AppConfig.defaultPage = AppConfig.DefaultPage.HOME; //默认首页为课表
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.getMessage());
                Toast.makeText(LoginActivity.this, "信息门户" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @OnCheckedChanged(R.id.cb_login_license) void checkLicense(boolean checked){
        if(checked){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("该APP为广财学生作品，非官方产品，但不会盗取和利用你的账号密码等信息，但若账号被盗，该产品不因此负任何责任。");
            builder.setTitle("使用需知");
            builder.setNeutralButton("不同意",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    cbLicense.setChecked(false);
                }
            });
            builder.setPositiveButton("我放心",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                }
            });
            builder.create().show();

        }

    }
    @OnClick(R.id.login_tv_pwd_same) void showJwPwdTextview() {
        if(edJwPwd.getVisibility() == View.VISIBLE){
            edJwPwd.setVisibility(View.GONE);
        }else{
            edJwPwd.setVisibility(View.VISIBLE);
        }
    }


}
