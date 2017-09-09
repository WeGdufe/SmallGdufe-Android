package com.guang.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享工具，注意 图片只能小于32K，大了会报 checkArgs fail, thumbData is invalid 错
 * Created by xiaoguang on 2017/9/9.
 * Github找的，基本没改动
 */

public class ShareUtils {
    /**
     * 收藏
     */
    public static final String TAG_FAVORITE = "TAG_FAVORITE";
    /**
     * 会话
     */
    public static final String TAG_SESSION = "TAG_SESSION";
    /**
     * 朋友圈
     */
    public static final String TAG_TIMELINE = "TAG_TIMELINE";

    /**
     * 未知，理论上不会用到
     */
    public static final String TAG_UNKNOWN = "TAG_UNKNOWN";

    @IntDef({SendMessageToWX.Req.WXSceneSession
            , SendMessageToWX.Req.WXSceneTimeline
            ,SendMessageToWX.Req.WXSceneFavorite})
    public @interface Scene{}

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private static final String APP_ID = AppConfig.WXShareAPP_ID;

    private static final String TAG = "ShareUtils";

    // IWXAPI 是第三方app和微信通信的openapi接口
    private static IWXAPI api;

    public static void install(Context context){
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context.getApplicationContext(),APP_ID, false);
        api.registerApp(APP_ID);
    }


    public static IWXAPI getApi(){
        return api;
    }

    public static void shareWeb(Context context,String title,String desc,String url,@Scene int scene){
        if (!checkForSupport(context)) return;
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = desc;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon_64);
        msg.thumbData = bmpToByteArray(thumb);

        // 如果分享到朋友圈需要校验是否支持
        if (scene == SendMessageToWX.Req.WXSceneTimeline && !checkTimeLine(context)) return;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(scene);
        req.message = msg;
        req.scene = scene;
        boolean sendReq = api.sendReq(req);
    }

    /**
     * 根据scene创建一个transaction字符串，从微信返回的时候会携带这个字符串
     * @param scene
     * @return
     */
    private static String buildTransaction(@Scene int scene) {
        String tag = null;
        if (scene == SendMessageToWX.Req.WXSceneSession){
            tag = TAG_SESSION;
        }else if (scene == SendMessageToWX.Req.WXSceneTimeline){
            tag = TAG_TIMELINE;
        }else if (scene == SendMessageToWX.Req.WXSceneFavorite){
            tag = TAG_FAVORITE;
        }else {
            // can't reach
            tag = TAG_UNKNOWN;
        }
        return tag + System.currentTimeMillis();
    }

    /**
     * 检测是否支持分享到朋友圈
     * @return
     */
    public static boolean checkTimeLine(Context context){
        int wxSdkVersion = api.getWXAppSupportAPI();
        boolean supportTimeLine = wxSdkVersion >= TIMELINE_SUPPORTED_VERSION;
        if (!supportTimeLine) Toast.makeText(context,"微信版本过低，请升级微信后分享",Toast.LENGTH_LONG).show();
        return supportTimeLine;
    }

    /**
     * 检测微信客户端是否可用
     *
     * @return
     */
    private static boolean checkForSupport(final Context context) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context,"尚未检测到微信，请安装微信后分享",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private static byte[] bmpToByteArray(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        bmp.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}