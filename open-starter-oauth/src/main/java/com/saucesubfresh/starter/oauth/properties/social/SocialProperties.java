package com.saucesubfresh.starter.oauth.properties.social;


import com.saucesubfresh.starter.oauth.properties.social.app.AppSocialProperties;
import com.saucesubfresh.starter.oauth.properties.social.mini.MiniSocialProperties;
import com.saucesubfresh.starter.oauth.properties.social.web.WebSocialProperties;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Administrator
 * @create 2020-03-23-18:25
 **/
@Data
public class SocialProperties {

    /**
     * app 第三方登录相关配置
     */
    @NestedConfigurationProperty
    private AppSocialProperties app = new AppSocialProperties();

    /**
     * 小程序 第三方登录相关配置
     */
    @NestedConfigurationProperty
    private MiniSocialProperties mini = new MiniSocialProperties();

    /**
     * 网页 第三方登录相关配置
     */
    @NestedConfigurationProperty
    private WebSocialProperties web = new WebSocialProperties();

}
