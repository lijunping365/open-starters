package com.lightcode.starter.oauth.properties;

import com.lightcode.starter.oauth.properties.social.SocialProperties;
import com.lightcode.starter.oauth.properties.token.TokenProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 安全相关配置
 */
@Data
@ConfigurationProperties(prefix = "com.lightcode.oauth")
public class OAuthProperties {

  /**
   * 认证服务器相关的配置
   */
  private TokenProperties token = new TokenProperties();

  /**
   * spring-social相关的配置
   */
  private SocialProperties social = new SocialProperties();


}
