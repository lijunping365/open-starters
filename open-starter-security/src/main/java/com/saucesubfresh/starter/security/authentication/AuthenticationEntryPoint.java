package com.saucesubfresh.starter.security.authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证
 * 处理 401
 * @author: 李俊平
 * @Date: 2022-05-04 09:30
 */
public interface AuthenticationEntryPoint {

    void commence(HttpServletRequest request) throws SecurityException;
}
