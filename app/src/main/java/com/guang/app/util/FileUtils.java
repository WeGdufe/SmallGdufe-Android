package com.guang.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.guang.app.AppConfig;
import com.guang.app.model.AppTips;
import com.guang.app.model.UserAccount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xiaoguang on 2017/2/20.
 */
public class FileUtils {
    private static final String DATA_FILE_DIR = "gdufe";
    private static final String AVATAR_FILE_NAME = "avatar.png";

    private static final String SP_FILE = "account_data";
    private static final String SP_SNO = "sno";
    private static final String SP_IDS_PSSWORD = "idsPwd";
    private static final String SP_JW_PSSWORD = "jwPwd";
    private static final String SP_DEFAULT_PAGE = "defaultPage";

    //启动时的温馨提示
    private static final String SP_TIPS_FILE = "app_tips";
    private static final String SP_TIPS_VERSION = "tipsVersion";


    private static final String SP_WEEK_FILE = "course_week";
    private static final String SP_CURRENT_WEEK = "currentWeek";
    private static final String SP_CURRENT_DAY = "currentDay";
    public static final int SP_WEEK_NOT_SET = -1;

    private static final String SP_JW_SETTINGS_FILE = "jw_settings";
    private static final String SP_SCORE_TARGET = "scoreQueryTarget";

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
     * 获取硬盘的缓存目录，用来放图片
     * http://blog.csdn.net/u011494050/article/details/39671159
     * @param context context
     * @return 路径
     */
    private static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            try{
                cachePath = context.getExternalCacheDir().getPath();//SDCard/Android/data/应用包名/cache/目录
            }catch (NullPointerException e){
                cachePath = context.getCacheDir().getPath();    //data/data/<application package>/cache
            }
        } else {
            cachePath = context.getCacheDir().getPath();    //data/data/<application package>/cache
        }
        return cachePath;
    }
    /**
     * 加载头像文件
     * @return Bitmap
     */
    public static Bitmap loadAvatarBitmap(Context context) {
        File appDir = new File(getDiskCacheDir(context));
        File file = new File(appDir, AVATAR_FILE_NAME);
        if(!file.exists()){
            return null;
        }
        return BitmapFactory.decodeFile(file.getPath());
    }
    public static void clearAvatarImage(Context context) {
        File appDir = new File(getDiskCacheDir(context));
        File file = new File(appDir, AVATAR_FILE_NAME);
        if(file.exists()){
            file.delete();
        }
    }
    /**
     * 保存图片文件（头像）
     * @param bmp Bitmap
     */
    public static void saveImage(Context context,Bitmap bmp) {
        File appDir = new File(getDiskCacheDir(context));
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = AVATAR_FILE_NAME;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    /**
     * 存文件里表示这个版本的tips不再提示
     * @param context
     * @param tips AppTips
     */
    public static void setTipsNeverShowAgain(Context context,AppTips tips){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_TIPS_FILE,0).edit();
        edit.putLong(SP_TIPS_VERSION,tips.getVersion());
        edit.apply();
    }
    /**
     * 返回存文件中的不再提示的tips的版本号，如果没有设置 不再提示 则返回 -1
     * @param context
     */
    public static long getTipsNeverShowAgainVersion(Context context,AppTips tips){
        SharedPreferences sp = context.getSharedPreferences(SP_TIPS_FILE,0);
        return sp.getLong(SP_TIPS_VERSION,-1);
    }
    public static void expireTipsNeverShowAgain(Context context){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_TIPS_FILE,0).edit();
        edit.clear();edit.apply();
    }

    public static void saveCurrentWeek(Context context,String week){
        String today = TimeUtils.getDateStringWithFormat("yyyy-MM-dd");
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_WEEK_FILE,0).edit();
        edit.putString(SP_CURRENT_WEEK,week);
        edit.putString(SP_CURRENT_DAY,today);
        edit.apply();
    }
    public static void expireCurrentWeek(Context context){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_WEEK_FILE,0).edit();
        edit.clear();
        edit.apply();
    }
    public static String getCurrentWeek(Context context){
        String savedWeek = getSavedWeek(context);
        if(savedWeek.equals(""+SP_WEEK_NOT_SET)){
            return ""+SP_WEEK_NOT_SET;
        }
        String today = TimeUtils.getDateStringWithFormat("yyyy-MM-dd");
        String savedDay = getSavedCurrentDay(context);
        long weekDiff = TimeUtils.weekBetweenTwoDateString(savedDay,today,"yyyy-MM-dd");
        long current = Long.parseLong(savedWeek) + weekDiff;
        if(current <= 0 || current > 16){
            current = 1;
        }
        return current+"";
    }

    private static String getSavedWeek(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_WEEK_FILE,0);
        return sp.getString(SP_CURRENT_WEEK,""+SP_WEEK_NOT_SET);
    }
    private static String getSavedCurrentDay(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_WEEK_FILE,0);
        String today = TimeUtils.getDateStringWithFormat("yyyy-MM-dd");
        return sp.getString(SP_CURRENT_DAY,today);
    }



    //查成绩，查主修还是辅修
    public static int getScoreQueryTarget(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_JW_SETTINGS_FILE,0);
        return sp.getInt(SP_SCORE_TARGET,0);
    }
    public static void setScoreQueryTarget(Context context,int target){
        SharedPreferences.Editor edit = context.getSharedPreferences(SP_JW_SETTINGS_FILE,0).edit();
        edit.putInt(SP_SCORE_TARGET,target);
        edit.apply();
    }
}
