package com.saucesubfresh.starter.captcha.send;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;

/**
 * @author lijunping on 2022/3/25
 */
@FunctionalInterface
public interface ValidateCodeSend<C extends ValidateCode> {

    void send(C validateCode);
}
