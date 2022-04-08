package com.saucesubfresh.starter.oauth.exception;

import com.saucesubfresh.starter.oauth.enums.OAuthExceptionEnum;
import lombok.Data;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证异常类
 */
@Data
public class AuthenticationException extends RuntimeException{

    private Integer code;

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(OAuthExceptionEnum OAuthExceptionEnum) {
        super(OAuthExceptionEnum.getMsg());
        this.code = OAuthExceptionEnum.getCode();
    }

    public AuthenticationException(Integer code, String message) {
        super(message);
        this.code = code;
    }


}
