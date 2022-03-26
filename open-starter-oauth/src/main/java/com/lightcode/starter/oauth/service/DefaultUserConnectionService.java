package com.lightcode.starter.oauth.service;

import com.lightcode.starter.oauth.domain.ConnectionDetails;

/**
 * @author lijunping on 2022/3/26
 */
public class DefaultUserConnectionService implements UserConnectionService{

    @Override
    public ConnectionDetails loadConnectionByProviderIdAndOpenId(String providerId, String openId) {
        return null;
    }

    @Override
    public void saveConnectionDetails(Long userId, String providerId, String openId) {

    }
}
