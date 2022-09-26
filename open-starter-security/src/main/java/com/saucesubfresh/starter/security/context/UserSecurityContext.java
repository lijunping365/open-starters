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

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lijunping
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
