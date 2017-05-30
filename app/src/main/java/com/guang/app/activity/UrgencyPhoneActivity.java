package com.guang.app.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.guang.app.R;
import com.guang.app.util.FileUtils;

import butterknife.Bind;

/**
 * 常用电话
 * Created by xiaoguang on 2017/2/19.
 */
public class UrgencyPhoneActivity extends QueryActivity {
    @Bind(R.id.tv_urgency_phone) TextView tv_phone;
    private boolean nowShowWhat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_chargeTime);
        setContentView(R.layout.urgency_phone);
        nowShowWhat = true;
    }

    @Override
    protected void loadData() {
        loadHtmlData(nowShowWhat);
    }

    //显示号码或者工作时间表
    private void loadHtmlData(boolean nowShowWhat){
        String content = "";
        if(!nowShowWhat){    //默认显示号码
            content = FileUtils.getStrFromAssets(this,"urgency_phone.html");
        }else{
            content = FileUtils.getStrFromAssets(this,"work_time.html");
        }
        tv_phone.setText(Html.fromHtml(content));
    }

    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_phone_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_charge_time:
                nowShowWhat = !nowShowWhat;
                loadHtmlData(nowShowWhat);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
