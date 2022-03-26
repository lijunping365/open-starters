package com.lightcode.starter.oauth.service;

import com.lightcode.starter.oauth.domain.UserDetails;

/**
 * @author lijunping on 2022/3/26
 */
public class DefaultUserDetailService implements UserDetailService{
    @Override
    public UserDetails loadUserByUsername(String username, String userType) {
        return null;
    }

    @Override
    public UserDetails loadUserByMobile(String mobile, String userType) {
        return null;
    }
}
