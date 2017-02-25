package com.guang.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.guang.app.R;

/**
 * 关于页面
 * Created by xiaoguang on 2017/2/13.
 */
public class AboutActivity extends QueryActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        addTitleBackBtn();

        super.onCreate(savedInstanceState);
    }
    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
}
