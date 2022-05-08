package com.saucesubfresh.starter.captcha.processor;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import com.saucesubfresh.starter.captcha.request.CaptchaGenerateRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码抽象处理器，包含验证码的生成处理，保存处理
 * @author : lijunping
 * @weixin : ilwq18242076871
 */
@Slf4j
public abstract class AbstractCaptchaGenerator<T extends ValidateCode> implements CaptchaGenerator<T> {

  private final CaptchaRepository captchaRepository;

  public AbstractCaptchaGenerator(CaptchaRepository captchaRepository) {
    this.captchaRepository = captchaRepository;
  }

  @Override
  public T create(CaptchaGenerateRequest request) throws ValidateCodeException {
    request.checkConstraints();
    T validateCode = this.generate();
    this.repository(request.getRequestId(), validateCode);
    return validateCode;
  }

  private void repository(String requestId, T validateCode){
    captchaRepository.save(requestId, validateCode);
  }

  protected abstract T generate() throws ValidateCodeException;
}
