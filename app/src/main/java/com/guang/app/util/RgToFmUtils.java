package com.guang.app.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Fragment管理类
 * Created by xiaoguang on 2017/2/14.
 */
public class RgToFmUtils {
    private int showindex;
    private int hideindex;
    private List<Fragment> fragmentList;
    private FragmentManager supportFragmentManager;
    private int id;
    private RadioGroup radioGroup;
    private Context context;

    //1.构造方法私有化
    private RgToFmUtils(Context context) {
        this.context = context;
    }

    //2.暴露出一个方法，返回当前类的对象
    private static RgToFmUtils mInstance;

    public static RgToFmUtils newInstance(Context context) {
        if (mInstance == null) {
            //实例化对象
            //加上一个同步锁，只能有一个执行路径进入
            synchronized (RgToFmUtils.class) {
                if (mInstance == null) {
                    mInstance = new RgToFmUtils(context);
                }
            }
        }
        return mInstance;
    }

    public void showTabToFragment(List<Fragment> fragmentList, RadioGroup radioGroup, FragmentManager supportFragmentManager, int id) {
        this.radioGroup = radioGroup;
        this.id = id;
        this.fragmentList = fragmentList;
        this.supportFragmentManager = supportFragmentManager;
        ((RadioButton) radioGroup.getChildAt(showindex)).setChecked(true);//初始化选中第一个
        showFragment(showindex, hideindex);//初始化碎片

        initsetOnClickListener();
    }

    /**
     * 作用：监听
     * name: Allens
     * created at 2016/9/4 10:23
     */
    private void initsetOnClickListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                radioButton.setChecked(true);
                int i = group.indexOfChild(radioButton);
                showFragment(i, hideindex);
                hideindex = i;
            }
        });
    }

    /**
     * 作用：显示碎片的逻辑
     * name: Allens
     * created at 2016/9/4 10:23
     */
    public void showFragment(int showindex, int hideindex) {
        Fragment showfragment = fragmentList.get(showindex);
        Fragment hidefragment = fragmentList.get(hideindex);
        FragmentTransaction ft = supportFragmentManager.beginTransaction();
        if (!showfragment.isAdded()) {
            ft.add(id, showfragment);
        }
        if (showindex == hideindex) {
            ft.show(showfragment);
        } else {
            ft.show(showfragment);
            ft.hide(hidefragment);
        }
        ft.commit();
    }
}
