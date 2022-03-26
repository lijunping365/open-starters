package com.lightcode.starter.oauth.core;


import com.lightcode.starter.oauth.request.BaseLoginRequest;
import com.lightcode.starter.oauth.token.AccessToken;

/**
 * @author lijunping on 2021/10/22
 */
public interface AuthenticationProcessor<T extends BaseLoginRequest> {

    AccessToken authentication(T request);
}
