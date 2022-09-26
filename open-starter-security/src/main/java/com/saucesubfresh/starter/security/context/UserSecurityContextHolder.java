/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.security.context;

/**
 * 参考 spring security 的 ThreadLocalSecurityContextHolderStrategy 类，简单实现。
 *
 * @author lijunping
 */
public class UserSecurityContextHolder {

  private static final ThreadLocal<UserSecurityContext> SECURITY_CONTEXT = new ThreadLocal<>();

  public static void setContext(UserSecurityContext context) {
    SECURITY_CONTEXT.set(context);
  }

  public static UserSecurityContext getContext() {
    return SECURITY_CONTEXT.get();
  }

  public static Long getUserId() {
    UserSecurityContext ctx = SECURITY_CONTEXT.get();
    return ctx != null ? ctx.getId() : null;
  }

  public static void clear() {
    SECURITY_CONTEXT.remove();
  }

}
