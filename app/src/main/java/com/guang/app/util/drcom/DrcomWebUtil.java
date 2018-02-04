package com.guang.app.util.drcom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaoguang on 2016/8/28.
 * 16年写出小工具，18年2月嵌在小广财里 不容易啊
 */
public class DrcomWebUtil {

    private static String TIPS_LOGIN_SUCCESS = "登陆成功";
    private static String TIPS_LOGOUT_SUCCESS = "注销成功";
    private static String TIPS_LOGIN_FAIL = "登陆失败";
    private static String TIPS_LOGOUT_FAIL = "注销失败";
    private static String TIPS_NO_CT = "账号或密码错误，或者非提速号";


    public static String translateWebReturn(String response){
        String res = "试试看能不能上网";
        Pattern pattern = Pattern.compile("Msg=(\\d*);");
        Matcher matcher = pattern.matcher(response);
        boolean isSuccess = true;
        while (matcher.find()) {
            isSuccess = false;
            String id = matcher.group(1);
            switch (Integer.parseInt(id)) {
                case 14:
                    res = TIPS_LOGIN_FAIL;
                    break;
                case 15:
                    res = TIPS_LOGIN_SUCCESS;
                    break;
                case 1:
                    res = TIPS_NO_CT;
                    break;
            }
        }
        if(isSuccess){
            res = TIPS_LOGIN_SUCCESS;
        }
        return res;
    }
}
