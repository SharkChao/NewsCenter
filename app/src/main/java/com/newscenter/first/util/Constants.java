package com.newscenter.first.util;

/**
 * Created by yuzhijun on 2018/4/3.
 */
public class Constants {
    public static final String PATH_URL = "/Path";
    public static final String BASE_URL =  "http://172.16.75.58:8088";
//    public static final String BASE_URL =  "http://192.168.43.133:8088";
    public static final String GIRLS_URL = "api/data/%E7%A6%8F%E5%88%A9/20/{index}/"+PATH_URL;

    public class HttpCode {
        public static final int HTTP_UNAUTHORIZED = 401;
        public static final int HTTP_SERVER_ERROR = 500;
        public static final int HTTP_NOT_HAVE_NETWORK = 600;
        public static final int HTTP_NETWORK_ERROR = 700;
        public static final int HTTP_UNKNOWN_ERROR = 800;
    }

    public static final String IS_LOGIN = "is_login";
    public static final String IS_NIGHT = "is_night";
    //收藏文章的列表
    public static final String IS_COLLECTION = "is_collection";
}
