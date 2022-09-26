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
package com.saucesubfresh.starter.security.properties;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全相关配置
 *
 * @author lijunping
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.security")
public class SecurityProperties {

  private static final String[] DEFAULT_IGNORE_PATHS = new String[]{
      // 静态资源 相关
      "/webjars/**", "/favicon.ico",
      // 登录相关
      "/login/**",
  };

  /**
   * redis token key 前缀
   */
  private String tokenPrefix = "access_token:";

  /**
   * 该字段需要配置
   * jwt 加密密钥 key，注意我们使用的是 SignatureAlgorithm.HS256，所以 secretKeyBytes.length * 8 必须大于 256
   */
  private String secretKey = "ThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKey";

  /**
   * 该属性不需要配置
   * secret key byte array.
   */
  private byte[] secretKeyBytes;

  public byte[] getSecretKeyBytes() {
    if (secretKeyBytes == null && secretKey != null) {
      secretKeyBytes = Decoders.BASE64.decode(secretKey);
    }
    return secretKeyBytes;
  }

  /**
   * 自定义忽略 Path， 白名单配置
   */
  private String[] ignorePaths = new String[0];
  /**
   * 默认忽略 Path
   */
  private String[] defaultIgnorePaths = DEFAULT_IGNORE_PATHS;

  public String[] getIgnorePaths() {
    return ignorePaths;
  }

  public SecurityProperties setIgnorePaths(String[] ignorePaths) {
    this.ignorePaths = ignorePaths;
    return this;
  }

  public String[] getDefaultIgnorePaths() {
    return defaultIgnorePaths;
  }

  public SecurityProperties setDefaultIgnorePaths(String[] defaultIgnorePaths) {
    this.defaultIgnorePaths = defaultIgnorePaths;
    return this;
  }

}
