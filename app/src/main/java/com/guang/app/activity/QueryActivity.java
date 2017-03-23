package com.guang.app.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.guang.app.R;
import com.guang.app.widget.RefreshActionItem;

/**
 * 各功能Activity的基类
 * 提供增加标题栏返回按钮功能
 * Created by xiaoguang on 2017/2/15.
 */
public class QueryActivity extends BaseActivity implements RefreshActionItem.RefreshButtonListener {
    protected RefreshActionItem mRefreshActionItem;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_query_actionbar, menu);
        MenuItem item = menu.findItem(R.id.menu_loading);
        if(shouldHideLoadingIcon()){
            item.setVisible(false);
            item.setEnabled(false);
        }
        mRefreshActionItem = (RefreshActionItem) item.getActionView();
        mRefreshActionItem.setMenuItem(item);
        mRefreshActionItem.setRefreshButtonListener(this);
        loadData();
        return true;
    }

    /**
     * 若具体业务需要隐藏loading图标，覆盖该方法返回true即可
     * @return true:隐藏 false[默认]:不隐藏
     */
    protected boolean shouldHideLoadingIcon() {
        return false;
    }
    /**
     * 具体业务实现的初始化数据，创建actionBar菜单后就被调用
     * 需要这个是因为onCreateOptionsMenu()调用在onCreate()之后，
     * 而我们APP很多功能需要一打开（在onCreate里）就跑数据load图标的，
     * 这会导致mRefreshActionItem是null报错，
     * 故加载数据方法放在onCreateOptionsMenu()而非业务的onCreate()
     */
    protected void loadData(){}

    /**
     * 添加标题栏返回按钮
     */
    public void addTitleBackBtn() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void startLoadingProgess(){
        mRefreshActionItem.startProgress();
    }
    public void stopLoadingProgess(){
        mRefreshActionItem.stopProgress();
    }
    /**
     * 添加标题栏返回按钮 实际效果代码
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(RefreshActionItem refreshActionItem) {
    }

//    public void hideLoadingProgress() {
//        mRefreshActionItem.setVisibility(View.GONE);
//    }
//    public void showLoadingProgress() {
//        mRefreshActionItem.setVisibility(View.VISIBLE);
//    }
}
