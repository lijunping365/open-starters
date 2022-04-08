package com.saucesubfresh.starter.oauth.properties.social;


import com.saucesubfresh.starter.oauth.properties.social.app.AppSocialProperties;
import com.saucesubfresh.starter.oauth.properties.social.mini.MiniSocialProperties;
import com.saucesubfresh.starter.oauth.properties.social.web.WebSocialProperties;
import lombok.Data;

/**
 * @author Administrator
 * @create 2020-03-23-18:25
 **/
@Data
public class SocialProperties {

    private AppSocialProperties app = new AppSocialProperties();

    private MiniSocialProperties mini = new MiniSocialProperties();

    private WebSocialProperties web = new WebSocialProperties();

}
