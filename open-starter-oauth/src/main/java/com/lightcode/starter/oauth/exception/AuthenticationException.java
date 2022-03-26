package com.lightcode.starter.oauth.exception;

import com.lightcode.starter.oauth.enums.ResultEnum;
import lombok.Data;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证异常类
 */
@Data
public class AuthenticationException extends RuntimeException{

    private Integer code;

    private String message;

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMsg();
    }

    public AuthenticationException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }


}
