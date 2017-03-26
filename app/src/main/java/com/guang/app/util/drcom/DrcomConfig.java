package com.guang.app.util.drcom;

public class DrcomConfig {
    public static final String AUTH_SERVER = "58.62.247.115";
    public static final String NET_HELP_WEB = "http://172.19.13.24:8089/Self/nav_login";
    public static final byte[] AUTH_VERSION = {0x7f,0x7f};  //最大化客户端验证版本号，客户端更新的话就不用改了
    public static final int PORT = 61440;
    public static final int TIMEOUT = 2000;//超时时间，单位毫秒，建议小点，因为本身的alive包就是间隔一段时间才发了
    public static final int ReconnectTIMES = 3;//重连次数
}
