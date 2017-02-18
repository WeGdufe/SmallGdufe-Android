package com.guang.app.activity;

import android.os.Bundle;

import com.guang.app.R;

import butterknife.ButterKnife;

/**
 * Created by xiaoguang on 2017/2/13.
 */
//@ContentView(R.layout.login)
public class LoginActivity extends BaseActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

}
