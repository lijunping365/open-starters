package com.saucesubfresh.starter.oauth.properties;

import com.saucesubfresh.starter.oauth.properties.social.SocialProperties;
import com.saucesubfresh.starter.oauth.properties.token.TokenProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 安全相关配置
 */
@Data
@ConfigurationProperties(prefix = "com.saucesubfresh.oauth")
public class OAuthProperties {

  /**
   * 认证服务器相关的配置
   */
  @NestedConfigurationProperty
  private TokenProperties token = new TokenProperties();

  /**
   * spring-social相关的配置
   */
  @NestedConfigurationProperty
  private SocialProperties social = new SocialProperties();

}
