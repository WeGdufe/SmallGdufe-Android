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
    private static final String SP_FILE = "account_data";

    private static final String SP_SNO = "sno";
    private static final String SP_IDS_PSSWORD = "idsPwd";
    private static final String SP_JW_PSSWORD = "jwPwd";
    private static final String SP_DEFAULT_PAGE = "defaultPage";

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
        AppConfig.defaultPage = userAccount.getDefaultPage();
        return !TextUtils.isEmpty(AppConfig.sno) && !TextUtils.isEmpty(AppConfig.idsPwd);
    }

    /**
     * 清除本地存储的账号信息
     * @param context
     */
    public static void expireStoredAccount(Context context){
        UserAccount userAccount = new UserAccount("","","");
        userAccount.setDefaultPage(AppConfig.DefaultPage.HOME);
        setStoredAccount(context,userAccount);
    }

    /**
     * 设置存储用户信息
     * @param context
     * @param userAccount
     */
    public static void setStoredAccount(Context context,UserAccount userAccount){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_FILE,0).edit();
        edit.putString(SP_SNO,userAccount.getSno());
        edit.putString(SP_IDS_PSSWORD,AESUtils.encryptLocal(userAccount.getIdsPwd()));
        edit.putString(SP_JW_PSSWORD,AESUtils.encryptLocal(userAccount.getJwPwd()));
        edit.putInt(SP_DEFAULT_PAGE,userAccount.getDefaultPage());
        edit.apply();
    }

    /**
     * 存储默认首页到文件
     * @param context
     * @param page
     */
    public static void setStoredDefaultPage(Context context,int page){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_FILE,0).edit();
        edit.putInt(SP_DEFAULT_PAGE,page);
        edit.apply();
    }
    /**
     * 读取默认首页
     * @param context
     * @return int page
     */
    public static int getStoredDefaultPage(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_FILE,0);
        return sp.getInt(SP_DEFAULT_PAGE, AppConfig.DefaultPage.HOME);
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
    public static UserAccount getStoredAccount(Context context){
        UserAccount userAccount = new UserAccount();
        SharedPreferences sp = context.getSharedPreferences(SP_FILE,0);
        userAccount.setSno(sp.getString(SP_SNO,null));
        userAccount.setIdsPwd(AESUtils.decryptLocal(sp.getString(SP_IDS_PSSWORD,"")));
        userAccount.setJwPwd(AESUtils.decryptLocal(sp.getString(SP_JW_PSSWORD,"")));
        userAccount.setDefaultPage(sp.getInt(SP_DEFAULT_PAGE, AppConfig.DefaultPage.HOME));
        return userAccount;
    }
}
