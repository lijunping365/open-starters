package com.lightcode.starter.captcha.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description:校验码处理请求实体类
 */
@Data
public class CaptchaVerifyRequest implements Serializable {
    private static final long serialVersionUID = -3596061866163459943L;

    @NotBlank(message = "请求唯一 id 不能为空")
    private String requestId;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
