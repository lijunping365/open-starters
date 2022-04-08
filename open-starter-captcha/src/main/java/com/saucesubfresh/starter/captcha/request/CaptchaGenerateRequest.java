package com.saucesubfresh.starter.captcha.request;

import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.utils.ValidatorUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 * Description: 验证码生成请求实体类
 */
@Data
@Accessors(chain = true)
public class CaptchaGenerateRequest implements Serializable {
    private static final long serialVersionUID = -3596061866163459943L;

    @NotBlank(message = "验证码类型不能为空, 验证码类型，可选值：sms，image，scan")
    private String type;

    @NotBlank(message = "请求唯一唯一 id 不能为空")
    private String requestId;

    /**
     * 在请求前检查自身的约束状况
     *
     * @throws ValidateCodeException .
     */
    public void checkConstraints() throws ValidateCodeException {
        try {
            ValidatorUtils.validate(this);
        } catch (ValidationException e) {
            throw new ValidateCodeException(e.getMessage());
        }
    }
}
