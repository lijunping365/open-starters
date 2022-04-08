package com.saucesubfresh.starter.oauth.token;


import lombok.Data;

import java.io.Serializable;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: token 实体类
 */
@Data
public class AccessToken implements Serializable {

  private static final long serialVersionUID = 2756099126284764479L;

  /**
   * accessToken
   */
  private String accessToken;

  /**
   * accessToken 过期时间
   */
  private String expiredTime;

}
