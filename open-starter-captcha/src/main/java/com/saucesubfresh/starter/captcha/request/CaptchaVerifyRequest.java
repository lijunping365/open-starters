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
 * Description:校验码处理请求实体类
 */
@Data
@Accessors(chain = true)
public class CaptchaVerifyRequest implements Serializable {
    private static final long serialVersionUID = -3596061866163459943L;

    @NotBlank(message = "请求唯一 id 不能为空")
    private String requestId;

    @NotBlank(message = "验证码不能为空")
    private String code;

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
