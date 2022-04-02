package com.lightcode.starter.security.interceptor;

import com.lightcode.starter.security.annotation.PreAuthorization;
import com.lightcode.starter.security.context.UserSecurityContext;
import com.lightcode.starter.security.context.UserSecurityContextHolder;
import com.lightcode.starter.security.domain.Authentication;
import com.lightcode.starter.security.enums.SecurityExceptionEnum;
import com.lightcode.starter.security.service.AuthorityService;
import com.lightcode.starter.security.service.TokenService;
import com.lightcode.starter.security.exception.SecurityException;
import com.lightcode.starter.security.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 认证通过且角色匹配的用户可访问当前路径
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 认证拦截器
 */
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor, BeanFactoryAware, InitializingBean {

  /**
   * 认证请求头
   */
  public static final String AUTHENTICATION_HEADER = "Authorization";
  /**
   * Basic Token 前缀
   */
  public static final String AUTHENTICATION_TYPE = "Bearer ";

  private AuthorityService authorityService;
  private final TokenService tokenService;
  private BeanFactory beanFactory;

  public SecurityInterceptor(TokenService tokenService){
    this.tokenService = tokenService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws SecurityException {
    String accessToken = extractTokenFromHeader(request);
    Authentication authentication = tokenService.readAuthentication(accessToken);
    UserSecurityContext user = JSON.parse(authentication.getUserDetails(), UserSecurityContext.class);
    UserSecurityContextHolder.setContext(user);
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    PreAuthorization authorization = handlerMethod.getMethodAnnotation(PreAuthorization.class);
    if (Objects.isNull(authorization)) {
      return true;
    }

    List<String> authorities = getAuthorities(request);
    if (!CollectionUtils.containsAny(authorities, user.getAuthorities())) {
      throw new SecurityException(SecurityExceptionEnum.FORBIDDEN);
    }

    return true;
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader(AUTHENTICATION_HEADER);
    if (header == null || !header.startsWith(AUTHENTICATION_TYPE)) {
      throw new SecurityException(SecurityExceptionEnum.NON_AUTHENTICATION);
    }
    return header.substring(7);
  }

  private List<String> getAuthorities(HttpServletRequest request){
    if (Objects.isNull(authorityService)){
      return Collections.emptyList();
    }

    PathMatcher pathMatcher = new AntPathMatcher();
    Map<String, List<String>> authoritiesMap = authorityService.getAuthorities();
    if (CollectionUtils.isEmpty(authoritiesMap)) {
      return Collections.emptyList();
    }

    List<String> authorities = new ArrayList<>();
    authoritiesMap.forEach((path, roles) ->{
      if (pathMatcher.match(path, request.getRequestURI())) {
        authorities.addAll(roles);
      }
    });
    return authorities;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    UserSecurityContextHolder.clear();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    try {
      this.authorityService = beanFactory.getBean(AuthorityService.class);
    }catch (BeansException e){
      log.warn("No AuthorityService instance is provided");
    }
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
