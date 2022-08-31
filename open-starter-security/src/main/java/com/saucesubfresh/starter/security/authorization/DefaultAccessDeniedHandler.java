package com.saucesubfresh.starter.security.authorization;

import com.saucesubfresh.starter.security.annotation.PreAuthorization;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import com.saucesubfresh.starter.security.enums.SecurityExceptionEnum;
import com.saucesubfresh.starter.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 授权
 * 处理 403
 * @author: 李俊平
 * @Date: 2022-05-04 09:28
 */
@Slf4j
public class DefaultAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public boolean handler(HttpServletRequest request, Object handler) throws SecurityException {
        if (!(handler instanceof HandlerMethod)){
            return Boolean.TRUE;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PreAuthorization authorization = handlerMethod.getMethodAnnotation(PreAuthorization.class);
        if (Objects.isNull(authorization)) {
            return Boolean.TRUE;
        }

        List<String> authorities = UserSecurityContextHolder.getContext().getAuthorities();
        if (!CollectionUtils.isEmpty(authorities) && authorities.contains(authorization.role())) {
            return Boolean.TRUE;
        }

        throw new SecurityException(SecurityExceptionEnum.FORBIDDEN);
    }
}
