package com.guang.app.util.drcom;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.DrcomActivity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by xiaoguang on 2017/3/22.
 */
public class DrcomService extends Service{
    public final static String INTENT_INFO = "info";

    private String mHostName = "Android";   //机器名，可随意
    private String mHostOs = "Android";   //系统名，可随意
    private String username;
    private String password;
    private HostInfo mHostInfo;
    private boolean isLogin = false; //是否登陆成功，用于注销和重复登陆判断

    private final static int FOREGROUND_ID = 1000;
    private NetWorkChangeReceiver mNetWorkReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHostInfo = (HostInfo)intent.getSerializableExtra(INTENT_INFO);
        username = mHostInfo.getUsername();
        password = mHostInfo.getPassword();
        startThread();

        return super.onStartCommand(intent, flags, startId);
    }
    //打日志，封装一下，方便修改，虽然没有行数显示了
    private void performLogCall(String msg){
        LogUtils.i(msg);
    }
    //需要显示给用户看的
    private void performMsgCall(final String msg){
        performLogCall(msg);
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            public void run(){
                Toast.makeText(getApplicationContext(), "Drcom:"+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //停止Thread : http://stackoverflow.com/questions/680180/where-to-stop-destroy-threads-in-android-service-class
    private  volatile Thread runner;
    private synchronized void startThread(){
        if(runner == null){
            runner = new Thread(new Runnable() {
                @Override
                public void run() {
                    mainRun();
                }
            });
            runner.start();
        }
    }
    private synchronized void stopThread(){
        if(runner != null){
            Thread moribund = runner;
            runner = null;
            moribund.interrupt();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!isNotificationEnabled(this)){
            performMsgCall("未开启通知栏权限，不影响使用，但看不到运行状态");
        }

        //注册网络改变监听器
        mNetWorkReceiver = new NetWorkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.registerReceiver(mNetWorkReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        performLogCall("onDestroy()");
        stopThread();
        cancelNotification();
        if(mNetWorkReceiver != null) {
            this.unregisterReceiver(mNetWorkReceiver);
        }
    }

    private void showNotification() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 1, new Intent(this, DrcomActivity.class), 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Dr.com正常运行中")
                        .setContentText("能看到我就说明有网络")
                        .setTicker("Dr.com运行中")
                        .setWhen(System.currentTimeMillis())
                        .setOngoing(false)      //用户无法滑动删除通知栏
                        .setContentIntent(contentIntent);
        Notification notification = mBuilder.build();
        startForeground(FOREGROUND_ID,notification);    //前台，防止被系统kill
        mNotifyMgr.notify(FOREGROUND_ID, notification);
    }
    private void cancelNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(FOREGROUND_ID);
    }

    private  boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Drcom 协议若干字段信息
    /**
     * 在 keep38 的回复报文中获得[28:30]
     */
    private static final byte[] keepAliveVer = {(byte) 0xdc, 0x02};
    /**
     * 官方客户端在密码错误后重新登录时，challenge 包的这个数会递增，因此这里设为静态的
     */
    private static int challengeTimes = 0;
    /**
     * 在 challenge 的回复报文中获得 [4:8]
     */
    private final byte[] salt = new byte[4];
    /**
     * 在 challenge 的回复报文中获得 [20:24], 当是有线网时，就是网络中心分配的 IP 地址，当时无线网时，是局域网地址
     */
    private final byte[] clientIp = new byte[4];
    /**
     * 在 login 的回复报文中获得[23:39]
     */
    private final byte[] tail1 = new byte[16];
    /**
     * 在 login 报文计算，计算时需要用到 salt,password
     */
    private final byte[] md5a = new byte[16];
    /**
     * 初始为 {0，0，0，0} , 在 keep40_1 的回复报文更新[16:20]
     */
    private final byte[] tail2 = new byte[4];
    /**
     * 在 keep alive 中计数.
     * 初始在 keep40_extra : 0x00, 之后每次 keep40 都加一
     */
    private int count = 0;
    private int keep38Count = 0;//仅用于日志计数
    private volatile boolean notifyLogout = false;
    private DatagramSocket client;
    private InetAddress serverAddress;

    private void mainRun() {
        if(isLogin){
            performMsgCall("重复登陆");
            return;
        }
        boolean exception = false;
        try {
            init();
            /**********************  challenge ***************************/
            if (!challenge(challengeTimes++)) {
                performLogCall("challenge # Server refused the request");
            }

            /**********************  login ***************************/
            if (!login()) {
                performLogCall("challenge # Failed to send authentication information.");
                return;
            }
            performMsgCall("登录成功");
            isLogin = true;
            showNotification();
            /**********************  KeepAlive ***************************/
            count = 0;
            int reconnectTimes = 0;

            long oldTimes = System.currentTimeMillis();//上一次重连/正常运行 开始的时间
            while (reconnectTimes <= DrcomConfig.ReconnectTIMES){
                try {
                    while (!notifyLogout
                            && !Thread.currentThread().isInterrupted()
                            && alive()) {   //收到注销通知或者调用了onDestory停止了线程则停止
                        Thread.sleep(20000);//每 20s 一次
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    performLogCall(e.getCause().getMessage());
                    reconnectTimes++;
                    //重连多次后放弃
                    if(reconnectTimes > DrcomConfig.ReconnectTIMES){
                        performMsgCall("从重连到放弃，准备断网");
                        break;
                    }
                    performMsgCall("通信超时，第"+reconnectTimes+"次自动重连");

                    //重置端口 继续发alive包，若超过则可能是UDP丢包导致
                    init();
                    if(reconnectTimes >= 2){
                        //第一次只重发alive，失败后之后第二三次选择重新登陆
                        if (!challenge(challengeTimes++)||!login()) {
                            performMsgCall("自动重新登陆失败，准备断网");
                            break;
                        }
                    }

                    //两次重连间相隔5分钟则认为是服务器或者其他非客户端导致的问题，重置重连次数
                    long curTimes = System.currentTimeMillis();
                    if( (curTimes-oldTimes)/(1000 * 60) > 5){
                        reconnectTimes = 0;
                    }
                    oldTimes = curTimes;
                }catch (InterruptedException e) {
                    performLogCall("因退出登陆而合理关闭线程，这里进行注销");
                    notifyLogout();
                    break;
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            performMsgCall("通信超时:"+e.getMessage());
            exception = true;
        } catch (IOException e) {
            performMsgCall("IO 异常"+e.getMessage());
            exception = true;
        } catch (Exception e) {
            exception = true;
        } finally {
            if (exception) {//若发生了异常：密码错误等。 则应允许重新登录
                cancelNotification();
                stopSelf(); //停止service的运行
                isLogin = false;
            }
            if (client != null) {
                client.close();
                client = null;
            }
        }
    }

    /**
     * 初始化套接字、设置超时时间、设置服务器地址
     */
    private void init()  {
        try {
            if(client != null){
                client.close();
                client = null;
            }
            client = new DatagramSocket();//客户端端口随机
            client.setSoTimeout(DrcomConfig.TIMEOUT);
            serverAddress = InetAddress.getByName(DrcomConfig.AUTH_SERVER);
        } catch (SocketException e) {
            performMsgCall("端口占用冲突"+e.getMessage());
        } catch (UnknownHostException e) {
            performMsgCall("认证服务器不通，确保你在校园网wifi内");
        }catch (Exception e){
            cancelNotification();
            e.printStackTrace();
            performMsgCall(e.getMessage());
        }
    }

    /**
     * 在回复报文中取得 salt 和 clientIp
     */
    private boolean challenge(int tryTimes) {
        try {
            //gdufe 0x2b是2017-03-20抓官方来的
            byte[] buf = {0x01, (byte) (0x02 + tryTimes), ByteUtil.randByte(), ByteUtil.randByte(), 0x22,
                    0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00};
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, DrcomConfig.PORT);
            client.send(packet);
            performLogCall("send challenge data: " + ByteUtil.toHexString(buf));
            buf = new byte[76]; //返回76大小
            packet = new DatagramPacket(buf, buf.length);
            client.receive(packet);
            if (buf[0] == 0x02) {
                performLogCall("recv challenge success, data: " + ByteUtil.toHexString(buf));
                // 保存 salt 和 clientIP
                System.arraycopy(buf, 4, salt, 0, 4);        //[4:8]
                System.arraycopy(buf, 20, clientIp, 0, 4);  //[20:24]
                return true;
            }
            performLogCall("challenge fail, unrecognized response. "+ ByteUtil.toHexString(buf));
            return false;
        } catch (SocketTimeoutException e) {
            performLogCall("Challenge超时");
        } catch (IOException e) {
            performMsgCall("认证失败");
        }
        return true;
    }
    
    private boolean login() throws IOException {
        byte[] buf = makeLoginPacket();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, DrcomConfig.PORT);
        
        client.send(packet);
        performLogCall("send login packet: "+ ByteUtil.toHexString(buf));
        byte[] recv = new byte[45];
        client.receive(new DatagramPacket(recv, recv.length));
        performLogCall("recv login packet: "+ ByteUtil.toHexString(recv));
        if (recv[0] != 0x04) {
            if (recv[0] == 0x05) {
                switch (recv[4]){
                    case 0x01:
                        performMsgCall("已在线");
                        break;
                    case 0x03:
                        performMsgCall("学号或密码错误");
                        break;
                    case 0x05:
                        performMsgCall("账号没钱啦");
                        break;
                    case 0x0B:
                        performMsgCall("MAC 地址错误");
                        break;
                    default:
                        performMsgCall("不知道什么错误");
                }
            } else {
                performMsgCall("不知名错误");
            }
            return false;
        }
        // 保存 tail1. 构造 keep38 要用 md5a(在mkptk中保存) 和 tail1
        // 注销也要用 tail1
        System.arraycopy(recv, 23, tail1, 0, 16);
        return true;
    }

    /**
     * 需要用来自 challenge 回复报文中的 salt, 构造报文时会保存 md5a keep38 要用
     */
    private byte[] makeLoginPacket() {
        byte code = 0x03;
        byte type = 0x01;
        byte EOF = 0x00;
        byte controlCheck = 0x20;
        byte adapterNum = 0x04; // Windows :07 gdufe 04
        byte ipDog = 0x01;
        byte[] primaryDNS = {(byte) 0xca, 0x60, (byte) 0x80, (byte) 0xa6};   //每台机子对应的dns和dhcp服务器，可以填0
        byte[] dhcp = {(byte) 0xde, (byte) 0xca, (byte) 0xab, 0x21};//同上
        byte[] md5b;

        int dataLen = 330;
        byte[] data = new byte[dataLen];

        data[0] = code;
        data[1] = type;
        data[2] = EOF;
        data[3] = (byte) (username.length() + 20);

        System.arraycopy(MD5.md5(new byte[]{code, type}, salt, password.getBytes()),
                0, md5a, 0, 16);//md5a保存起来
        System.arraycopy(md5a, 0, data, 4, md5a.length);//md5a 4+16=20

        byte[] user = ByteUtil.ljust(username.getBytes(), 36);
        System.arraycopy(user, 0, data, 20, user.length);//username 20+36=56

        data[56] = controlCheck;//0x20
        data[57] = adapterNum;

        //md5a[0:6] xor mac
        System.arraycopy(md5a, 0, data, 58, 6);
        byte[] macBytes = mHostInfo.getMacBytes();
        for (int i = 0; i < macBytes.length; i++) { //macBytes.length=6
            data[i + 58] ^= macBytes[i];//md5a oxr mac
        }// xor 58+6=64
        md5b = MD5.md5(new byte[]{0x01}, password.getBytes(), salt, new byte[]{0x00, 0x00, 0x00, 0x00});
        System.arraycopy(md5b, 0, data, 64, md5b.length);//md5b 64+16=80

        data[80] = 0x01;//网卡数量，直接填1后面就有12个byte（3个网卡ip）可以直接填0处理了
        System.arraycopy(clientIp, 0, data, 81, clientIp.length);//ip1 81+4=85
        System.arraycopy(new byte[]{
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00,
        }, 0, data, 85, 12);//ip2/3/4 85+12=97

        data[97] = 0x14;//临时放，97 ~ 97+8 是 md5c[0:8]
        data[98] = 0x00;
        data[99] = 0x07;
        data[100] = 0x0b;
        byte[] tmp = new byte[101];
        System.arraycopy(data, 0, tmp, 0, 101);//前 97 位 和 0x14_00_07_0b
        byte[] md5c = MD5.md5(tmp);
        System.arraycopy(md5c, 0, data, 97, 8);//md5c 97+8=105

        data[105] = ipDog;//0x01
        //fill four zero.  106+4=110
        byte[] hostname = ByteUtil.ljust(mHostName.getBytes(), 32);
        System.arraycopy(hostname, 0, data, 110, 32);//hostname 110+32=142
        System.arraycopy(primaryDNS, 0, data, 142, 4);//primaryDNS 142+4=146
        System.arraycopy(dhcp, 0, data, 146, 4);//dhcp 146+4=150
        dhcp[3] = 0x1f;
        System.arraycopy(dhcp, 0, data, 150, 4);//dhcp 150+4=154 //second dns 150+4=154 填0就不写了

        //delimiter 154+8=162 填0就不写了

        data[162] = (byte) 0x94;//unknown 162+4=166
        data[166] = 0x06;       //os major 166+4=170
        data[170] = 0x02;       //os minor 170+4=174
        data[174] = (byte) 0xf0;//os build
        data[175] = 0x23;       //os build 174+4=178
        data[178] = 0x02;       //os unknown 178+4=182

        byte[] byteHostOs = ByteUtil.ljust(mHostOs.getBytes(),32);
        System.arraycopy(byteHostOs, 0, data, 182, byteHostOs.length);//byteHostOs 182+32=214
        //214+96=310 byte zero，实际不全是0
        data[310] = DrcomConfig.AUTH_VERSION[0];
        data[311] = DrcomConfig.AUTH_VERSION[1];
        //这附近相对原本的吉林大学版修改较大
        data[312] = 0x02;
        data[313] = 0x0c;

        //这部分开始是 checksum(data+'\x01\x26\x07\x11\x00\x00'+dump(mac)) 占4个byte
        byte[] temp = {0x01,0x26,0x07,0x11,0x00,0x00};
        tmp = new byte[326];//data+'\x01\x26\x07\x11\x00\x00'+dump(mac)
        System.arraycopy(data, 0, tmp, 0, 314);
        System.arraycopy(temp, 0, tmp, 314, temp.length);            //len=6
        System.arraycopy(macBytes, 0, tmp, 320, macBytes.length);    //len(macbyte)=6
        tmp = ByteUtil.checksum(tmp);
        System.arraycopy(tmp, 0, data, 314, 4); //取checksum前4个
        //checksum结束，4个byte

        data[318] = 0x00;//0分割，占2个
        data[319] = 0x00;

        //macBytes占6个
        System.arraycopy(macBytes, 0, data, 320, macBytes.length);//320+6=326
        data[326] = 0x00;             //Auto_Logout
        data[327] = 0x00;            //broadcast mode
        data[328] = (byte) 0xc2;    //unknown 可填0
        data[329] = 0x66;           //unknown 可填0
        return data;
    }

    private boolean alive() throws IOException {
        init();
        boolean needExtra = false;
        ++keep38Count;
        performLogCall("count = "+count+", keep38count = " +keep38Count);
        if (count % 21 == 0) {//第一个 keep38 后有 keep40_extra, 十个 keep38 后 count 就加了21
            needExtra = true;
        }//每10个keep38

        //-------------- keep38 ----------------------------------------------------
        byte[] packet38 = makeKeepPacket38();
        DatagramPacket packet = new DatagramPacket(packet38, packet38.length, serverAddress, DrcomConfig.PORT);
        
        client.send(packet);
        performLogCall("[rand="+ByteUtil.toHexString(packet38[36])+"|"+ByteUtil.toHexString(packet38[37])+"]"
                +"send keep38.:"+ByteUtil.toHexString(packet38));

        byte[] recv = new byte[64];
        client.receive(new DatagramPacket(recv, recv.length));
        String logStr = String.format("[rand={%s}|{%s}]recv Keep38. [{%s}.{%s}.{%s}.{%s}] 【{%s}】",
                ByteUtil.toHexString(recv[6]), ByteUtil.toHexString(recv[7]),
                ByteUtil.toInt(recv[12]), ByteUtil.toInt(recv[13]), ByteUtil.toInt(recv[14]), ByteUtil.toInt(recv[15]),
                ByteUtil.toHexString(recv));
        performLogCall(logStr);
        keepAliveVer[0] = recv[28];//收到keepAliveVer//通常不会变
        keepAliveVer[1] = recv[29];

        needExtra = false;  //不发送keep40_extra貌似不影响广财的上网
        if (needExtra) {//每十次keep38都要发一个 keep40_extra
            performLogCall("Keep40_extra...");
            //--------------keep40_extra--------------------------------------------
            //先发 keep40_extra 包
            byte[] packet40extra = makeKeepPacket40(1, true);
            packet = new DatagramPacket(packet40extra, packet40extra.length, serverAddress, DrcomConfig.PORT);
            client.send(packet);
            logStr = String.format("[seq={%s}|type={%s}][rand={%s}|{%s}]send Keep40_extra. 【{%s}】",
                    packet40extra[1], packet40extra[5],
                    ByteUtil.toHexString(packet40extra[8]), ByteUtil.toHexString(packet40extra[9]),
                    ByteUtil.toHexString(packet40extra));
            performLogCall(logStr);
            recv = new byte[40];
            client.receive(new DatagramPacket(recv, recv.length));
            logStr = String.format("[seq={%s}|type={%s}][rand={%s}|{%s}]recv Keep40_extra. 【{%s}】",
                    recv[1], recv[5], ByteUtil.toHexString(recv[8]), ByteUtil.toHexString(recv[9]), ByteUtil.toHexString(recv));
            performLogCall(logStr);
            //不理会回复
        }

        //--------------keep40_1----------------------------------------------------
        byte[] packet40_1 = makeKeepPacket40(1, false);
        packet = new DatagramPacket(packet40_1, packet40_1.length, serverAddress, DrcomConfig.PORT);
        
        client.send(packet);
        logStr = String.format("[seq={%s}|type={%s}][rand={%s}|{%s}]send Keep40_1. 【{%s}】",
                packet40_1[1], packet40_1[5],
                ByteUtil.toHexString(packet40_1[8]), ByteUtil.toHexString(packet40_1[9]),
                ByteUtil.toHexString(packet40_1));
        performLogCall(logStr);
        recv = new byte[40];//40
        client.receive(new DatagramPacket(recv, recv.length));
        logStr = String.format("[seq={%s}|type={%s}][rand={%s}|{%s}]send Keep40_1. 【{%s}】",
                recv[1], recv[5],
                ByteUtil.toHexString(recv[8]), ByteUtil.toHexString(recv[9]), ByteUtil.toHexString(recv));
        performLogCall(logStr);

        //保存 tail2 , 待会儿构造 packet 要用
        System.arraycopy(recv, 16, tail2, 0, 4);

        //--------------keep40_2----------------------------------------------------
        byte[] packet40_2 = makeKeepPacket40(2, false);
        packet = new DatagramPacket(packet40_2, packet40_2.length, serverAddress, DrcomConfig.PORT);
        
        client.send(packet);
        logStr = String.format("[seq={%s}|type={%s}][rand={%s}|{%s}]send Keep40_2. 【{%s}】",
                packet40_2[1], packet40_2[5],
                ByteUtil.toHexString(packet40_2[8]), ByteUtil.toHexString(packet40_2[9]),
                ByteUtil.toHexString(packet40_2));
        performLogCall(logStr);
        recv = new byte[40];
        client.receive(new DatagramPacket(recv, recv.length));
//        performLogCall("[seq={%s}|type={%s}][rand={%s}|{%s}]recv Keep40_2. 【{%s}】", recv[1], recv[5],
//                ByteUtil.toHexString(recv[8]), ByteUtil.toHexString(recv[9]), ByteUtil.toHexString(recv));
        //keep40_2 的回复也不用理会
        return true;
    }

    /**
     * 0xff md5a:16位 0x00 0x00 0x00 tail1:16位 rand rand
     */
    private byte[] makeKeepPacket38() {
        byte[] data = new byte[38];
        data[0] = (byte) 0xff;
        System.arraycopy(md5a, 0, data, 1, md5a.length);//1+16=17
        //17 18 19
        System.arraycopy(tail1, 0, data, 20, tail1.length);//20+16=36
        data[36] = ByteUtil.randByte();
        data[37] = ByteUtil.randByte();
        return data;
    }

    /**
     * keep40_额外的 就是刚登录时, keep38 后发的那个会收到 272 This Program can not run in dos mode
     * keep40_1     每 秒发送
     * keep40_2
     */
    private byte[] makeKeepPacket40(int firstOrSecond, boolean extra) {
        byte[] data = new byte[40];
        data[0] = 0x07;
        data[1] = (byte) count++;//到了 0xff 会回到 0x00
        data[2] = 0x28;
        data[3] = 0x00;
        data[4] = 0x0b;
        //   keep40_1   keep40_2
        //  发送  接收  发送  接收
        //  0x01 0x02 0x03 0xx04
        if (firstOrSecond == 1 || extra) {//keep40_1 keep40_extra 是 0x01
            data[5] = 0x01;
        } else {
            data[5] = 0x03;
        }
        if (extra) {
            data[6] = 0x0f;
            data[7] = 0x27;
        } else {
            data[6] = keepAliveVer[0];
            data[7] = keepAliveVer[1];
        }
        data[8] = ByteUtil.randByte();
        data[9] = ByteUtil.randByte();

        //[10-15]:0

        System.arraycopy(tail2, 0, data, 16, 4);//16+4=20

        //20 21 22 23 : 0

        if (firstOrSecond == 2) {
            System.arraycopy(clientIp, 0, data, 24, 4);
            byte[] tmp = new byte[28];
            System.arraycopy(data, 0, tmp, 0, tmp.length);
            tmp = ByteUtil.crc(tmp);
            System.arraycopy(tmp, 0, data, 24, 4);//crc 24+4=28

            System.arraycopy(clientIp, 0, data, 28, 4);//28+4=32
            //之后 8 个 0
        }
        return data;
    }

    public  void notifyLogout() {
        notifyLogout = true;//终止 keep 线程
        performLogCall("收到注销指令");
        if (isLogin) {//已登录才注销
            boolean succ = true;
            try {
                challenge(challengeTimes++);
                succ = logout();
            } catch (Throwable t) {
                t.printStackTrace();
                succ = false;
                t.printStackTrace();
                performMsgCall("注销异常");
            } finally {
                if (succ) {
                    performMsgCall("注销成功");
                    cancelNotification();
                    stopSelf(); //停止service的运行
                }
                if (client != null) {
                    client.close();
                    client = null;
                }
            }
        }
    }

    private boolean logout() throws IOException {
        byte[] buf = makeLogoutPacket();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, DrcomConfig.PORT);
        
        client.send(packet);
        performLogCall("send logout packet."+ ByteUtil.toHexString(buf));

        byte[] recv = new byte[512];//25
        client.receive(new DatagramPacket(recv, recv.length));
        performLogCall("recv logout packet response."+ByteUtil.toHexString(recv));
        if (recv[0] == 0x04) {
            return true;
        }
        return false;
    }

    private byte[] makeLogoutPacket() {
        byte[] data = new byte[80];
        data[0] = 0x06;//code
        data[1] = 0x01;//type
        data[2] = 0x00;//EOF
        data[3] = (byte) (username.length() + 20);
        byte[] md5 = MD5.md5(new byte[]{0x06, 0x01}, salt, password.getBytes());
        System.arraycopy(md5, 0, data, 4, md5.length);//md5 4+16=20
        System.arraycopy(ByteUtil.ljust(username.getBytes(), 36),
                0, data, 20, 36);//username 20+36=56
        data[56] = 0x20;
        data[57] = 0x05;
        byte[] macBytes = mHostInfo.getMacBytes();
        for (int i = 0; i < 6; i++) {
            data[58 + i] = (byte) (data[4 + i] ^ macBytes[i]);
        }// mac xor md5 58+6=64
        System.arraycopy(tail1, 0, data, 64, tail1.length);//64+16=80
        return data;
    }

    /**
     * 监听网络改变情况，关闭wifi、切换到其他wifi 时退出登录
     */
    class NetWorkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //wifi更换时判断目标是不是学校wifi,不是的话就停掉
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {  //连上wifi

                    WifiUtils wifiUtils = new WifiUtils(context);
                    boolean isSchoolWifi = DrcomActivity.currentIsSchoolWifi(wifiUtils);
                    if(!isSchoolWifi){
                        performMsgCall("wifi切换，已退出登陆");
                        cancelNotification();
                        stopSelf(); //停止service
                        return;
                    }
                }
            }
            //wifi关闭就直接停掉
            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    performMsgCall("wifi已关闭，自动退出登陆");
                    cancelNotification();
                    stopSelf(); //停止service
                }
    //            if (wifistate == WifiManager.WIFI_STATE_ENABLED) WIFI开启
            }
        }
    }
}

