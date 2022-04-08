package com.saucesubfresh.starter.http.constants;

/**
 * @author: 李俊平
 * @Date: 2021-07-22 10:33
 */
public interface HttpConstant {

    abstract class UserAgent {

        public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
        public static final String USER_AGENT_FIREFOX_45 = "Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0";
        public static final String USER_AGENT_IE = "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko";
        public static final String USER_AGENT_EDGE = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586";

    }

    abstract class ContentType {

        public static final String JSON = "application/json";

        public static final String XML = "text/xml";

        public static final String HTML = "text/html";

        public static final String TEXT = "text/plain";

        public static final String JSONP = "text/javascript";

        public static final String FORM = "application/x-www-form-urlencoded";

        public static final String MULTIPART = "multipart/form-data";
    }



    enum Method {

        GET,

        HEAD,

        POST,

        PUT,

        DELETE,

        TRACE,

        CONNECT,
    }

    abstract class StatusCode {

        public static final int CODE_200 = 200;

        public static final int CODE_202 = 202;

        public static final int CODE_400 = 400;

        public static final int CODE_401 = 401;

        public static final int CODE_402 = 402;

        public static final int CODE_403 = 403;

        public static final int CODE_404 = 404;

        public static final int CODE_500 = 500;

    }

    abstract class Header {

        public static final String ACCEPT = "Accept";

        public static final String CONTENT_TYPE = "Content-Type";

        public static final String REFERER = "Referer";

        public static final String USER_AGENT = "User-Agent";

        public static final String COOKIE = "Cookie";
    }

    abstract class Charset{

        public static final String GBK = "gbk";

        public static final String UTF_8 = "utf-8";
    }
}
