package com.lightcode.starter.oauth.service;


import com.lightcode.starter.oauth.domain.UserDetails;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 用户信息接口
 */
public interface UserDetailService {

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 通过手机号获取用户信息
     * @param mobile 手机号
     * @return
     */
    UserDetails loadUserByMobile(String mobile);
}
