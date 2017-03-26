package com.guang.app;

public class AppConfig {

    public static String sno = "";           // 用户学号
    public static String idsPwd = "";
    public static String jwPwd = "";
    public static String localAesSeed = "local";
    public static String netAesSeed = "netdsad238139dsad";

    //千万记得这个URL要/结尾！！！
//    public static final String BASE_URL = "http://172.25.155.14:82/";
//    public static final String BASE_URL = "http://192.168.137.1:82/";
    public static final String BASE_URL = "http://www.wintercoder.com:82/";
    public static final String Avator_URL = "https://www.94cb.com/Material-Design-Avatars/avatar.php?char=";

    public static class Url {
        public static final String index = "index.php?r=";
        public static final String getScore = index + "jw/get-grade";
        public static final String getSchedule = index + "jw/get-schedule";
        public static final String getBasicInfo = index + "jw/get-basic";

        public static final String fewSztz = index + "info/few-sztz";

        public static final String getXiaoLi = index + "jwc/get-xiaoli";
        public static final String getCet = index + "jwc/get-cet";

        public static final String currentBook = index + "opac/current-book";
        public static final String borrowedBook = index + "opac/borrowed-book";
        public static final String searchBook = index + "opac/search-book";
        public static final String getRenewBookVerify = index + "opac/get-renew-book-verify";
        public static final String renewBook = index + "opac/renew-book";
        public static final String getBookStoreDetail = index + "opac/get-book-store-detail";

        public static final String feedback = index + "work/feedback";

        public static final String getCurrentCash = index + "card/current-cash";
        public static final String cardConsumeToday = index + "card/consume-today";

//        public static String yesterdayBalance = index + "info/info-tips";
        public static final String updateURL = index + "work/check-app-update";

    }

}
