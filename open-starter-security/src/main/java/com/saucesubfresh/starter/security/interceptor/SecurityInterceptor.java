package com.saucesubfresh.starter.security.interceptor;


import com.saucesubfresh.starter.security.authentication.AuthenticationEntryPoint;
import com.saucesubfresh.starter.security.authorization.AccessDeniedHandler;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import com.saucesubfresh.starter.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 认证通过且角色匹配的用户可访问当前路径
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证拦截器
 */
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor{

  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final AccessDeniedHandler accessDeniedHandler;

  public SecurityInterceptor(AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws SecurityException {
    authenticationEntryPoint.commence(request);
    return accessDeniedHandler.handler(request, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    UserSecurityContextHolder.clear();
  }
}
