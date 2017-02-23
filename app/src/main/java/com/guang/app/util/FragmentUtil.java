package com.guang.app.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * fragment管理类
 * Created by xiaoguang on 2017/2/23.
 */
public class FragmentUtil {
    private static FragmentUtil instance;

    public FragmentActivity act;
    private FragmentManager fm;

    public static FragmentUtil init(FragmentActivity act) {
        if (instance == null||instance.act != act) {
            instance = new FragmentUtil(act);
        }
        return instance;
    }

    public FragmentUtil(FragmentActivity act) {
        this.act = act;
        fm = act.getFragmentManager();
    }

    public void replace(int containerId, Fragment fragment) {
        if (currentFragment==fragment) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment);
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    public void replaceHasHistory(int containerId, Fragment fragment) {
        if (currentFragment==fragment) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment);
        ft.addToBackStack(null);//添加这个会在按返回键的时候先退出fragment，一些特殊场合可能用到，但效果其实不理想
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    /**记录已添加的fragment*/
    private ArrayList<Fragment> fs = new ArrayList<Fragment>();
    /**记录当前show的fragment*/
    private Fragment currentFragment;
    /**添加fragment，这中方式的fragment需要用show来显示*/
    public void add(int containerId, Fragment fragment){
        if (!fs.contains(fragment)) {
            fs.add(fragment);
            fm.beginTransaction().add(containerId, fragment).commit();
        }
    }
    /**添加带tag的fragment*/
    public void add(int containerId, Fragment fragment, String tag){
        if (!fs.contains(fragment)) {
            fs.add(fragment);
            fm.beginTransaction().add(containerId, fragment, tag).commit();
        }
    }
    public void addAll(int containerId, List<Fragment> fragments){
        for (Fragment fragment: fragments) {
            add(containerId,fragment);
        }
    }
//    public void addAll(int containerId, List<Fragment> fragments){
//        fs.addAll(fragments);
//        FragmentTransaction ft = fm.beginTransaction();
//        for (int i = 0; i < fs.size(); i++) {
//            ft.add(i,fs.get(i));
//        }
//        ft.commit();
//    }
    /**使用本方法前必须先add*/
    public void show(Fragment fragment){
        if (currentFragment==fragment) {
            return;//如果是当前fragment，则不重新show一遍了，无意义
        }
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment f:fs) {
            ft.hide(f);
        }
        ft.show(fragment);
        ft.commit();
        currentFragment = fragment;
    }
    /**移除add过的fragment*/
    public void remove(Fragment fragment){
        if (fs.contains(fragment)) {
            fs.remove(fragment);
            fm.beginTransaction().remove(fragment).commit();
        }
    }
    /**清空所有fragment*/
    public void removeAll(){
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment f:fs) {
            ft.remove(f);
        }
        ft.commitAllowingStateLoss();
    }
    /**清空所有带此tag的fragment*/
    public void removeAll(String tag){
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment f:fs) {
            if(f.getTag().equals(tag))ft.remove(f);
        }
        ft.commitAllowingStateLoss();
    }
    /**清空所有除了带此tag的fragment*/
    public void removeAllExcept(String tag){
        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment f:fs) {
            if(f.getTag()!=null&&!f.getTag().equals(tag))ft.remove(f);
        }
        ft.commitAllowingStateLoss();
    }
}
