package com.lightcode.starter.oauth.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 手机号验证码登录
 */
@Data
public class MobileLoginRequest extends BaseLoginRequest implements Serializable {

    private static final long serialVersionUID = 2512743958725643646L;

    @NotBlank(message = "手机号不能为空")
    private String mobile;

}
