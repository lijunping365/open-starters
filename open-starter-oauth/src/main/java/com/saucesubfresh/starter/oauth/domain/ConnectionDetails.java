package com.saucesubfresh.starter.oauth.domain;

import java.io.Serializable;

/**
 * 用户社交信息
 * @author: 李俊平
 * @Date: 2021-10-14 07:35
 */
public interface ConnectionDetails extends Serializable {

    /**
     * 服务提供商
     *
     * @return
     */
    String getProviderId();

    /**
     * openId
     *
     * @return
     */
    String getOpenId();

    /**
     * 绑定用户 id
     *
     * @return
     */
    Long getUserId();

    /**
     * 是否绑定
     *
     * @return
     */
    boolean isBind();

}
