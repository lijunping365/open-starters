package com.lightcode.starter.security.properties;

import io.jsonwebtoken.io.Decoders;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 安全相关配置
 */
@Data
@ConfigurationProperties(prefix = "com.lightcode.security")
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
