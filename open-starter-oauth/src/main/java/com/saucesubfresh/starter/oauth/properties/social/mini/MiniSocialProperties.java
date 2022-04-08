package com.saucesubfresh.starter.oauth.properties.social.mini;

import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class MiniSocialProperties {

  private WeAppProperties weapp = new WeAppProperties();

  private ByteDanceProperties tt = new ByteDanceProperties();

  private AliPayProperties alipay = new AliPayProperties();
}
