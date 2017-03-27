package com.guang.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.guang.app.R;
import com.guang.app.util.CalcUtils;

import butterknife.Bind;

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
        tvVersion.setText("广财小工具 v" + CalcUtils.getVersionName(this));
        super.onCreate(savedInstanceState);
    }
    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
}
