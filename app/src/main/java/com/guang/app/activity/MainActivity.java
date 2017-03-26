package com.guang.app.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.fragment.FeatureFragment;
import com.guang.app.fragment.HomeFragment;
import com.guang.app.fragment.MeFragment;
import com.guang.app.util.FileUtils;
import com.guang.app.util.FragmentUtil;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
//    @Bind(R.id.main_fragment)
//    FrameLayout mFramLayout;
    private List<Fragment> mFragments;
    @Bind(R.id.tab_radioGroup) RadioGroup mTabGroup;
    private FragmentUtil fUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        if(!FileUtils.getStoredAccountAndSetApp(this) || TextUtils.isEmpty(AppConfig.sno) || TextUtils.isEmpty(AppConfig.idsPwd)){
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
            return; //没这个居然会往下跑！
        }
        setTitle(R.string.app_name);
        initFragment();
        UpdateBuilder.create().check();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new FeatureFragment());
//        mFragments.add(new ShareFragment());  //备用
        mFragments.add(new MeFragment());

        fUtil = FragmentUtil.init(this);
        fUtil.addAll(R.id.main_fragment,mFragments);
        fUtil.show(mFragments.get(0));
        mTabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                switch (checkID){
                    case R.id.rd_home:
                        fUtil.show(mFragments.get(0));break;
                    case R.id.rd_features:
                        fUtil.show(mFragments.get(1));break;
                    case R.id.rd_me:
                        fUtil.show(mFragments.get(2));break;
                }
            }
        });
    }

}