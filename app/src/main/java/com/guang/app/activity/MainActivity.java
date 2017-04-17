package com.guang.app.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.fragment.FeatureFragment;
import com.guang.app.fragment.HomeFragment;
import com.guang.app.fragment.MeFragment;
import com.guang.app.model.UserAccount;
import com.guang.app.util.FileUtils;
import com.guang.app.util.FragmentUtil;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
//    @Bind(R.id.main_fragment)
//    FrameLayout mFramLayout;
    private List<Fragment> mFragments;
//    @Bind(R.id.tab_radioGroup) RadioGroup mTabGroup;
    @Bind(R.id.rd_home) RadioButton radioHome;
    @Bind(R.id.rd_features) RadioButton radioFeature;   //用于默认首页时的radiobutton选择情况（颜色高亮）
    @Bind(R.id.rd_me) RadioButton radioMe;

    private FragmentUtil fUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        TODO 删
        FileUtils.setStoredAccount(MainActivity.this, new UserAccount("13251102210", "123456","123456"));
        AppConfig.defaultPage = AppConfig.DefaultPage.HOME; //默认首页为课表

        //未登录跳转登陆页
        if(!FileUtils.getStoredAccountAndSetApp(this) || TextUtils.isEmpty(AppConfig.sno) || TextUtils.isEmpty(AppConfig.idsPwd)){
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();return; //没这个居然会往下跑！
        }
        //默认页为drcom的话就跳转到drcom页且关闭当前的（特例：从drcom回到main的情况不跳转，不然就死循环跳转了）
        AppConfig.defaultPage = FileUtils.getStoredDefaultPage(this);
        if(AppConfig.defaultPage == AppConfig.DefaultPage.DRCOM
                && !getIntent().getBooleanExtra(DrcomActivity.INTENT_DRCOM_TO_MAIN,false) ) {
            startActivity(new Intent(this, DrcomActivity.class));
            this.finish();return;
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

//        mTabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
//                switch (checkID){
//                    case R.id.rd_home:
//                        fUtil.show(mFragments.get(0));break;
//                    case R.id.rd_features:
//                        fUtil.show(mFragments.get(1));break;
//                    case R.id.rd_me:
//                        fUtil.show(mFragments.get(2));break;
//                }
//            }
//        });
        //启动默认首页，注意跟上面监听器的顺序问题
        AppConfig.defaultPage = FileUtils.getStoredDefaultPage(this);
        switch (AppConfig.defaultPage){
            case AppConfig.DefaultPage.HOME:
                radioHome.setChecked(true);
                break;
            case AppConfig.DefaultPage.FEATURE:
                radioFeature.setChecked(true);
                break;
            case AppConfig.DefaultPage.ME:
                radioMe.setChecked(true);
                break;
            default:    //从drcom返回要设置首页显示，隐藏其他，否则全部fragment叠加
                radioFeature.setChecked(true);
                break;
        }
    }

    @OnClick({ R.id.rd_home, R.id.rd_features,R.id.rd_me }) public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rd_home:
                if (checked) {
                    fUtil.show(mFragments.get(0));break;
                }
            case R.id.rd_features:
                if (checked) {
                    fUtil.show(mFragments.get(1));break;
                }
            case R.id.rd_me:
                if (checked) {
                    fUtil.show(mFragments.get(2));break;
                }
        }
    }
}