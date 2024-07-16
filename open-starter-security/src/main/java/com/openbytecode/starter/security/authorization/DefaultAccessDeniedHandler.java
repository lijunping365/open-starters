/*
 * Copyright © 2022 organization openbytecode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openbytecode.starter.security.authorization;

import com.openbytecode.starter.security.annotation.PreAuthorization;
import com.openbytecode.starter.security.context.UserSecurityContextHolder;
import com.openbytecode.starter.security.exception.AccessDeniedException;
import com.openbytecode.starter.security.exception.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 授权
 * 处理 403
 *
 * @author lijunping
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

        throw new AccessDeniedException("Permission denied");
    }
}
