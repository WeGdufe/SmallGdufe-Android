package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.guang.app.R;
import com.guang.app.api.OpacApiFactory;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 查找图书
 * Created by xiaoguang on 2017/2/18.
 */
public class SearchBookActivity extends QueryActivity {
    private static OpacApiFactory factory = OpacApiFactory.getInstance();

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.common_listview);
        ButterKnife.bind(this);
        initAdapterAndData();
    }

    private void initAdapterAndData() {
    }

}