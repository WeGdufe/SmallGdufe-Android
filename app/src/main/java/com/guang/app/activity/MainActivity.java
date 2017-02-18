package com.guang.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.guang.app.R;
import com.guang.app.api.InfoApiFactory;
import com.guang.app.fragment.FeatureFragment;
import com.guang.app.fragment.HomeFragment;
import com.guang.app.fragment.MeFragment;
import com.guang.app.util.RgToFmUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
//    @Bind(R.id.main_fragment)
//    FrameLayout mFramLayout;
    private List<Fragment> mFragments;
    @Bind(R.id.tab_radioGroup) RadioGroup mTabGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        initFragment();
        test();
    }

    private void test() {
          InfoApiFactory factory = InfoApiFactory.getInstance();

//        utils.api.create(InfoApi.class).getFewSztz()
//                .map(new ApiUtils.HttpResultFunc<List<FewSztz>>())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(sub);

    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new FeatureFragment());
//        mFragments.add(new ShareFragment());
        mFragments.add(new MeFragment());
        RgToFmUtils.newInstance(this).showTabToFragment(mFragments, mTabGroup, getFragmentManager(), R.id.main_fragment);
        RgToFmUtils.newInstance(this).showFragment(1,0);
//        RgToFmUtils.newInstance(this).showTabToFragment(mFragments, mTabGroup, getFragmentManager(), R.id.main_fragment);
    }
}