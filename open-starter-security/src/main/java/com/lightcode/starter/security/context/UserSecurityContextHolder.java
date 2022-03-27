package com.lightcode.starter.security.context;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 参考 spring security 的 ThreadLocalSecurityContextHolderStrategy 类，简单实现。
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
