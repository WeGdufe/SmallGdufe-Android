package com.guang.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.guang.app.AppConfig;
import com.guang.app.model.UserAccount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xiaoguang on 2017/2/20.
 */
public class FileUtils {
    private static final String spfile_account = "account_data";

    /**
     * 获取存储在本地的账号信息，返回是否有存储(有登陆过)
     * @param context
     * @return boolean 有登陆的情况下返回true
     */
    public static boolean getStoredAccountAndSetApp(Context context){
        UserAccount userAccount = getStoredAccount(context);
        AppConfig.sno = userAccount.getSno();
        AppConfig.idsPwd = userAccount.getIdsPwd();
        AppConfig.jwPwd = userAccount.getJwPwd();
        return !TextUtils.isEmpty(AppConfig.sno) && !TextUtils.isEmpty(AppConfig.idsPwd);
    }

    /**
     * 清除本地存储的账号信息
     * @param context
     */
    public static void expireStoredAccount(Context context){
        UserAccount userAccount = new UserAccount("","","");
        setStoredAccount(context,userAccount);
    }
    private static UserAccount getStoredAccount(Context context){
        UserAccount userAccount = new UserAccount();
        SharedPreferences sp = context.getSharedPreferences(spfile_account,0);
        userAccount.setSno(sp.getString("sno",null));
        userAccount.setIdsPwd(AESUtils.decryptLocal(sp.getString("idsPwd",null)));
        userAccount.setJwPwd(AESUtils.decryptLocal(sp.getString("jwPwd",null)));
        return userAccount;
    }

    public static void setStoredAccount(Context context,UserAccount userAccount){
        SharedPreferences.Editor edit = context.getSharedPreferences(spfile_account,0).edit();
        edit.putString("sno",userAccount.getSno());
        edit.putString("idsPwd",AESUtils.encryptLocal(userAccount.getIdsPwd()));
        edit.putString("jwPwd",AESUtils.encryptLocal(userAccount.getJwPwd()));
        edit.apply();
    }

    /**
     * 读取main/assets目录文件
     * @param context
     * @param filename
     * @return
     */
    public static String getStrFromAssets(Context context, String filename) {
        try {
            String line = "";
            String Result = "";
            InputStream file = context.getResources().getAssets().open(filename);
            InputStreamReader inputReader = new InputStreamReader(file);
            BufferedReader bufReader = new BufferedReader(inputReader);
            while ((line = bufReader.readLine()) != null) Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
