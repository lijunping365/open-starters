package com.saucesubfresh.starter.security.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 用户信息
 */
@Data
public class UserDetails implements Serializable {
    private static final long serialVersionUID = 7779447586729787146L;

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 角色列表
     */
    private List<String> authorities;

    /**
     * 账号是否锁定 true 锁定，false 非锁定
     */
    private Boolean accountLocked;
}
