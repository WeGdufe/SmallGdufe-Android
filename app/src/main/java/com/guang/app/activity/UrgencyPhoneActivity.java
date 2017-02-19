package com.guang.app.activity;

import android.os.Bundle;

import com.guang.app.R;

import butterknife.ButterKnife;

/**
 * 常用电话
 * Created by xiaoguang on 2017/2/19.
 */
public class UrgencyPhoneActivity extends QueryActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_urgencyPhone);
        super.addTitleBackBtn();
        setContentView(R.layout.xiaoli);
        ButterKnife.bind(this);
    }

    @Override
    protected void loadData() {
        startLoadingProgess();
        stopLoadingProgess();
    }
}
