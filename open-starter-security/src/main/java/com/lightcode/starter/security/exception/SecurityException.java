package com.lightcode.starter.security.exception;

import com.lightcode.starter.security.enums.SecurityExceptionEnum;
import lombok.Data;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证异常类
 */
@Data
public class SecurityException extends RuntimeException{

    private int code;

    public SecurityException(String msg){
        super(msg);
    }

    public SecurityException(int code) {
        super();
        this.code = code;
    }

    public SecurityException(SecurityExceptionEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public SecurityException(int code, String message) {
        super(message);
        this.code = code;
    }


    /**
     * 禁止访问
     */
    public static final int FORBIDDEN = 403;

    /**
     * 请求头中认证信息不能为空
     */
    public static final int NON_AUTHENTICATION = 402;

    /**
     * 认证信息错误或已失效
     */
    public static final int UNAUTHORIZED = 401;

}
