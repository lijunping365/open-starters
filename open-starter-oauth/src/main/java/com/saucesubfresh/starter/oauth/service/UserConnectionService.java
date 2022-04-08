package com.saucesubfresh.starter.oauth.service;

import com.saucesubfresh.starter.oauth.domain.ConnectionDetails;

/**
 * 用户社交信息服务
 * @author: 李俊平
 * @Date: 2021-10-14 07:34
 */
public interface UserConnectionService {

    /**
     * 根据 providerId 和 openId 在connection 表中查找是否存在一条 connection 数据
     *
     * @param providerId
     * @param openId
     * @return
     */
    ConnectionDetails loadConnectionByProviderIdAndOpenId(String providerId, String openId);

    /**
     * 保存 userId 和社交信息到 在connection 表
     *
     * @param userId
     * @param providerId
     * @param openId
     */
    void saveConnectionDetails(Long userId, String providerId, String openId);
}
