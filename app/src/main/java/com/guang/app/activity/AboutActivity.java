package com.guang.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.util.CalcUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于页面
 * Created by xiaoguang on 2017/2/13.
 */
public class AboutActivity extends QueryActivity {
    @Bind(R.id.tv_about_version) TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        addTitleBackBtn();
        tvVersion.setText(getResources().getString(R.string.app_name) + " v" + CalcUtils.getVersionName(this));
        super.onCreate(savedInstanceState);
    }
    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }

    @OnClick(R.id.tv_qgroup) void joinQGroup(){
        joinQQGroup("h5e6PrDHmrbINR1ZaVcy0I-LvrKFxhIM");
    }

    /****************
     *
     * 发起添加群流程。群号：移动广财用户群(631036490) 的 key 为： h5e6PrDHmrbINR1ZaVcy0I-LvrKFxhIM
     * 调用 joinQQGroup(h5e6PrDHmrbINR1ZaVcy0I-LvrKFxhIM) 即可发起手Q客户端申请加群 移动广财用户群(631036490)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    private boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(this, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
