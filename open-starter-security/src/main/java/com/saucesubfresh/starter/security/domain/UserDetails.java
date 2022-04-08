package com.saucesubfresh.starter.security.domain;

import lombok.Data;

import java.io.Serializable;

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
}
