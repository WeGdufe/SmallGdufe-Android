package com.guang.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.util.FileUtils;
import com.guang.app.util.drcom.DrcomConfig;
import com.guang.app.util.drcom.DrcomFileUtils;
import com.guang.app.util.drcom.DrcomService;
import com.guang.app.util.drcom.HostInfo;
import com.guang.app.util.drcom.WifiUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class DrcomActivity extends QueryActivity {
    public static String INTENT_DRCOM_TO_MAIN = "drcom2main";   // 标记是从drcom返回main的，否则main根据默认页又会继续跳回来

    @Bind(R.id.ed_drcom_username)  EditText edUsername;
    @Bind(R.id.ed_drcom_password)  EditText edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drcom);

        addTitleBackBtn();

        //获取本地存储的账号，填到文本框里
        HostInfo info = DrcomFileUtils.getStoredAccount(this);
        if(null != info){
            edUsername.setText(info.getUsername());
            edPassword.setText(info.getPassword());
            //第一次打开的时候没有存储学号，此时显示APP的学号信息
            if(TextUtils.isEmpty(edUsername.getText())){
                edUsername.setText(AppConfig.sno);
                if(AppConfig.schoolmateSno.equals(AppConfig.sno)) {
                    edUsername.setText("为不影响体验账号，校友禁用该功能");
                }
            }
        }
    }

    @OnClick(R.id.btn_drcom_login) void drcomLogin() {
        if(AppConfig.schoolmateSno.equals(AppConfig.sno)){
            Toast.makeText(this, "校友/外校生禁用该功能", Toast.LENGTH_SHORT).show();
            return;
        }
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
        boolean isSchoolWifi = currentIsSchoolWifi(wifiUtils);
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
        if(AppConfig.schoolmateSno.equals(AppConfig.sno)){
            Toast.makeText(this, "校友/外校生禁用该功能", Toast.LENGTH_SHORT).show();
            return;
        }
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

    //重写返回按钮，如果是默认页面为drcom则按返回 返回到主activity（因为main->drcom已经finish了main，所以得new）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppConfig.defaultPage = FileUtils.getStoredDefaultPage(this);
                if(AppConfig.defaultPage == AppConfig.DefaultPage.DRCOM){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(INTENT_DRCOM_TO_MAIN, true);
                    startActivity(intent);
                    this.finish();
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    /**
     * 判断当前wifi是不是学校wifi
     * @param wifiUtils
     * @return
     */
    public static boolean currentIsSchoolWifi(WifiUtils wifiUtils) {
        final String drcomSSid[] = {"gdufe","gdufe-teacher","Young"}; //能上网的wifi名
        String wifiName = wifiUtils.getSSID();
        wifiName = wifiName.replace("\"","");   //4.0以上的getSSID返回 "gdufe" 带了引号
        boolean isSchoolWifi = false;
        for (String ssidName: drcomSSid ) {
            if (ssidName.equalsIgnoreCase(wifiName)) {
                isSchoolWifi = true;
                break;
            }
        }
        return isSchoolWifi;
    }
}

