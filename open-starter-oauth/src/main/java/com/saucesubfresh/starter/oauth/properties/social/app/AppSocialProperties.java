package com.saucesubfresh.starter.oauth.properties.social.app;

import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class AppSocialProperties {

  private WxSocialProperties wx = new WxSocialProperties();
}
