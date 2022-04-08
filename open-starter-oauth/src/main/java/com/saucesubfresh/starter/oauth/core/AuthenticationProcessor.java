package com.saucesubfresh.starter.oauth.core;


import com.saucesubfresh.starter.oauth.request.BaseLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;

/**
 * @author lijunping on 2021/10/22
 */
public interface AuthenticationProcessor<T extends BaseLoginRequest> {

    AccessToken authentication(T request);
}
