package com.lightcode.starter.oauth.properties.social.web;

import lombok.Data;

/**
 * @author: 李俊平
 * @Date: 2021-03-03 19:54
 */
@Data
public class WebSocialProperties {

  private QQProperties qq = new QQProperties();

  private WeChatProperties weChat = new WeChatProperties();

  private AliPayProperties alipay = new AliPayProperties();
}
