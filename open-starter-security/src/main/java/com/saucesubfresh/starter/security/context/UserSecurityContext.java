package com.saucesubfresh.starter.security.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 上下文对象
 */
@Data
@Accessors(chain = true)
public class UserSecurityContext implements Serializable {
  private static final long serialVersionUID = 3069248878500028106L;

  /**
   * 用户 id
   */
  private Long id;

  /**
   * 用户名称
   */
  private String username;

  /**
   * 用户手机号
   */
  private String mobile;

  /**
   * 用户的角色列表
   * 例如： ["admin", "super_admin"]
   */
  private List<String> authorities;

  /**
   * 账号是否锁定 true 锁定，false 非锁定
   */
  private Boolean accountLocked;
}
