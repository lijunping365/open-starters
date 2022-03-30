package com.lightcode.starter.security.interceptor;

import com.lightcode.starter.security.annotation.PreAuthorization;
import com.lightcode.starter.security.context.UserSecurityContext;
import com.lightcode.starter.security.context.UserSecurityContextHolder;
import com.lightcode.starter.security.domain.Authentication;
import com.lightcode.starter.security.service.AuthorityService;
import com.lightcode.starter.security.service.TokenService;
import com.lightcode.starter.security.exception.SecurityException;
import com.lightcode.starter.security.utils.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
@Data
public class SecurityInterceptor implements HandlerInterceptor {

  /**
   * 认证请求头
   */
  public static final String AUTHENTICATION_HEADER = "Authorization";
  /**
   * Basic Token 前缀
   */
  public static final String AUTHENTICATION_TYPE = "Bearer ";

  private final TokenService tokenService;
  private final AuthorityService authorityService;

  public SecurityInterceptor(TokenService tokenService, AuthorityService authorityService){
    this.tokenService = tokenService;
    this.authorityService = authorityService;
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
      throw new SecurityException("没有该操作权限");
    }
    return true;
  }

  private String extractTokenFromHeader(HttpServletRequest request) {
    String header = request.getHeader(AUTHENTICATION_HEADER);
    if (header == null || !header.startsWith(AUTHENTICATION_TYPE)) {
      throw new SecurityException("非法的请求头");
    }
    return header.substring(7);
  }

  private List<String> getAuthorities(HttpServletRequest request){
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
}
