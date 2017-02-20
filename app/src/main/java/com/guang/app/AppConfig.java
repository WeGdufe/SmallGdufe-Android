package com.guang.app;

public class AppConfig {
//    static boolean DEBUG = true;
//    public static String sno = "13251102210";           // 用户学号
//    public static String idsPwd = "qq5521140";
//    public static String jwPwd = "qq5521140";

    public static String sno = "";           // 用户学号
    public static String idsPwd = "";
    public static String jwPwd = "";
    public static String localAesSeed = "local";
    public static String netAesSeed = "netdsad238139dsad";

//    public static String sno = "13251102217";           // 用户学号
//    public static String idsPwd = "118118";
//    public static String jwPwd = "118118";

    //    public static final String BASE_URL = "http://www.wintercoder.com:82";
//    public static final String BASE_URL = "http://192.168.1.105/";
    public static final String BASE_URL = "http://192.168.1.105:82/";

    public static class Url {
        public static final String index = "index.php?r=";
        public static final String getScore = index + "jw/get-grade";

        public static final String fewSztz = index + "info/few-sztz";

        public static final String getXiaoLi = index + "jwc/get-xiaoli";

        public static final String currentBook = index + "opac/current-book";
        public static final String borrowedBook = index + "opac/borrowed-book";
        public static final String searchBook = index + "opac/search-book";
    }

}
