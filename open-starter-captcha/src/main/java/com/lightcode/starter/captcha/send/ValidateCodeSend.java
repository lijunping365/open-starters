package com.lightcode.starter.captcha.send;

import com.lightcode.starter.captcha.core.sms.ValidateCode;

/**
 * @author lijunping on 2022/3/25
 */
@FunctionalInterface
public interface ValidateCodeSend {

    <C extends ValidateCode> void send(C validateCode);
}
