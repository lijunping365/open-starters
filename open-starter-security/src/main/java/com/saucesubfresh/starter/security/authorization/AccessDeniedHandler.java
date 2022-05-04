package com.saucesubfresh.starter.security.authorization;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权
 * 处理 403
 * @author: 李俊平
 * @Date: 2022-05-04 09:28
 */
public interface AccessDeniedHandler {

    boolean handler(HttpServletRequest request, Object handler) throws SecurityException;
}
