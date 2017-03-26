package com.guang.app.util.drcom;

import android.content.Context;
import android.content.SharedPreferences;

import com.guang.app.util.AESUtils;

/**
 * drcom账号信息存储存储，不存读mac地址
 * Created by xiaoguang on 2017/3/25.
 */
public class DrcomFileUtils {
    private static final String SP_FILE = "drcom_sp_file";
    private static final String SP_USERNAME = "username";
    private static final String SP_PASSWORD = "password";

    /**
     * 清除本地存储的账号信息
     * @param context
     */
    public static void expireStoredAccount(Context context) {
        HostInfo info = new HostInfo();
        info.setUsername("");
        info.setPassword("");
        setStoredAccount(context, info);
    }

    /**
     * 获取存储过的drcom账号信息，无则返回空学号空密码的对象
     * @param context
     * @return
     */
    public static HostInfo getStoredAccount(Context context) {
        HostInfo info = new HostInfo();
        SharedPreferences sp = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
        info.setUsername(sp.getString(SP_USERNAME, ""));
        info.setPassword(AESUtils.decryptLocal(sp.getString(SP_PASSWORD, "")));
        return info;
    }

    /**
     * 存储drcom账号信息（不存mac地址）
     * @param context
     * @param info
     */
    public static void setStoredAccount(Context context, HostInfo info) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE).edit();
        edit.putString(SP_USERNAME, info.getUsername());
        edit.putString(SP_PASSWORD, AESUtils.encryptLocal(info.getPassword()));
        edit.apply();
    }

}
