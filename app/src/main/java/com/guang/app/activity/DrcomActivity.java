package com.guang.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.util.drcom.DrcomConfig;
import com.guang.app.util.drcom.DrcomFileUtils;
import com.guang.app.util.drcom.DrcomService;
import com.guang.app.util.drcom.HostInfo;
import com.guang.app.util.drcom.WifiUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrcomActivity extends QueryActivity {

    @Bind(R.id.ed_drcom_username)  EditText edUsername;
    @Bind(R.id.ed_drcom_password)  EditText edPassword;

    private final String drcomSSid[] = {"gdufe","gdufe-teacher","Young"}; //能上网的wifi名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drcom);
        ButterKnife.bind(this);
        addTitleBackBtn();

        //获取本地存储的账号，填到文本框里
        HostInfo info = DrcomFileUtils.getStoredAccount(this);
        if(null != info){
            edUsername.setText(info.getUsername());
            edPassword.setText(info.getPassword());
        }
    }

    @OnClick(R.id.btn_drcom_login) void drcomLogin() {
        String username = edUsername.getText().toString();
        String password = edPassword.getText().toString();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "别闹，学号密码呢", Toast.LENGTH_SHORT).show();
            return;
        }

        //检查wifi是否打开且是否为学校wifi
        WifiUtils wifiUtils = new WifiUtils(this);
        if(!wifiUtils.isWifiOpened()){
            Toast.makeText(this, "wifi未打开", Toast.LENGTH_SHORT).show();
            return;
        }
        String wifiName = wifiUtils.getSSID();
        wifiName = wifiName.replace("\"","");   //4.0以上的getSSID返回 "gdufe" 带了引号
        boolean isSchoolWifi = false;
        for (String ssidName: drcomSSid ) {
            if (ssidName.equalsIgnoreCase(wifiName)) {
                isSchoolWifi = true;
                break;
            }
        }
        if(!isSchoolWifi){
            Toast.makeText(this, "未连接学校wifi", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取现在的mac地址并且启动service
        String mac = wifiUtils.getMacAddress();
        HostInfo info = new HostInfo(username,password, mac);
        Intent startIntent = new Intent(this, DrcomService.class);
        startIntent.putExtra(DrcomService.INTENT_INFO,info);
        startService(startIntent);

        //存储信息（不含mac）
        DrcomFileUtils.setStoredAccount(this,info);
    }

    @OnClick(R.id.btn_drcom_logout) void drcomLogut() {
        stopService(new Intent(this, DrcomService.class));
    }

    //网络自助平台，启动自带浏览器去打开
    @OnClick(R.id.tv_drcom_net_help) void netHelp() {
        Uri uri = Uri.parse(DrcomConfig.NET_HELP_WEB);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
}

