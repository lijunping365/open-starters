package com.lightcode.starter.captcha.processor;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import com.lightcode.starter.captcha.exception.ValidateCodeException;
import com.lightcode.starter.captcha.generator.CaptchaGenerator;
import com.lightcode.starter.captcha.generator.ValidateCodeGenerator;
import com.lightcode.starter.captcha.repository.CaptchaRepository;
import com.lightcode.starter.captcha.request.CaptchaGenerateRequest;
import com.lightcode.starter.captcha.request.CaptchaVerifyRequest;
import com.lightcode.starter.captcha.send.ValidateCodeSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 *
 * Description: 验证码抽象处理器，包含验证码的生成处理，保存处理，发送处理，验证处理
 */
@Slf4j
public class DefaultCaptchaProcessor<C extends ValidateCode> implements CaptchaProcessor, ApplicationContextAware, InitializingBean {

  private final Map<String, ValidateCodeGenerator<C>> validateCodeGeneratorMap = new ConcurrentHashMap<>();
  private final CaptchaRepository captchaRepository;
  private ApplicationContext applicationContext;

  public DefaultCaptchaProcessor(CaptchaRepository captchaRepository) {
    this.captchaRepository = captchaRepository;
  }

  @Override
  public void create(CaptchaGenerateRequest request, ValidateCodeSend validateCodeSend) throws Exception {
    C validateCode = validateCodeGeneratorMap.get(request.getType()).generate();
    save(request.getRequestId(), validateCode);
    validateCodeSend.send(validateCode);
  }

  @Override
  public void validate(CaptchaVerifyRequest request) {
    String validateCode = captchaRepository.get(request.getRequestId());
    String codeInRequest = request.getCode();

    if (StringUtils.isBlank(validateCode)) {
      throw new ValidateCodeException("验证码已过期");
    }

    if (!StringUtils.equals(validateCode, codeInRequest)) {
      throw new ValidateCodeException("验证码输入错误");
    }
  }

  @Override
  public void afterPropertiesSet() {
    Map<String, Object> beans = applicationContext.getBeansWithAnnotation(CaptchaGenerator.class);
    beans.forEach((k,v)->{
      CaptchaGenerator annotation = v.getClass().getAnnotation(CaptchaGenerator.class);
      validateCodeGeneratorMap.put(annotation.value(), (ValidateCodeGenerator<C>) v);
    });
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  private void save(String requestId, C validateCode) {
    ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
    captchaRepository.save(requestId, code);
  }
}
