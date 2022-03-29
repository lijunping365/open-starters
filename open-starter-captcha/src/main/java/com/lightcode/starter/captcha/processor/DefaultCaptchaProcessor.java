package com.lightcode.starter.captcha.processor;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import com.lightcode.starter.captcha.exception.ValidateCodeException;
import com.lightcode.starter.captcha.generator.ValidateCodeGenerator;
import com.lightcode.starter.captcha.repository.CaptchaRepository;
import com.lightcode.starter.captcha.request.CaptchaGenerateRequest;
import com.lightcode.starter.captcha.request.CaptchaVerifyRequest;
import com.lightcode.starter.captcha.send.ValidateCodeSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 *
 * Description: 验证码抽象处理器，包含验证码的生成处理，保存处理，验证处理
 */
@Slf4j
public class DefaultCaptchaProcessor implements CaptchaProcessor{

  private static final String GENERATOR_SUFFIX = "CodeGenerator";
  private final Map<String, ValidateCodeGenerator> validateCodeGeneratorMap;
  private final CaptchaRepository captchaRepository;

  public DefaultCaptchaProcessor(Map<String, ValidateCodeGenerator> validateCodeGeneratorMap, CaptchaRepository captchaRepository) {
    this.validateCodeGeneratorMap = validateCodeGeneratorMap;
    this.captchaRepository = captchaRepository;
  }

  @Override
  public void create(CaptchaGenerateRequest request, ValidateCodeSend validateCodeSend) throws Exception {
    request.checkConstraints();
    String generatorName = request.getType() + GENERATOR_SUFFIX;
    ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(generatorName);
    if (Objects.isNull(validateCodeGenerator)){
      throw new ValidateCodeException(String.format("未找到[%s]类型验证码生成器", generatorName));
    }
    ValidateCode validateCode = validateCodeGenerator.generate();
    captchaRepository.save(request.getRequestId(), validateCode);
    validateCodeSend.send(validateCode);
  }

  @Override
  public void validate(CaptchaVerifyRequest request) {
    request.checkConstraints();
    String validateCode = captchaRepository.get(request.getRequestId());
    String codeInRequest = request.getCode();

    if (StringUtils.isBlank(validateCode)) {
      throw new ValidateCodeException("验证码已过期");
    }

    if (!StringUtils.equals(validateCode, codeInRequest)) {
      throw new ValidateCodeException("验证码输入错误");
    }
  }
}
