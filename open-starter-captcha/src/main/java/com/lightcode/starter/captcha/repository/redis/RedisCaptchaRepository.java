package com.lightcode.starter.captcha.repository.redis;

import com.lightcode.starter.captcha.core.sms.ValidateCode;
import com.lightcode.starter.captcha.repository.CaptchaRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

/**
 * @author : lijunping
 * @weixin : ilwq18242076871
 *
 * Description：使用redis+deviceId的方式进行验证码的存、取、删
 */
public class RedisCaptchaRepository implements CaptchaRepository {

  private final StringRedisTemplate stringRedisTemplate;

  public RedisCaptchaRepository(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  @Override
  public <C extends ValidateCode> void save(String requestId, C code) {
    stringRedisTemplate.opsForValue().set(requestId, code.getCode(), code.getExpireTime(), TimeUnit.MINUTES);
  }

  @Override
  public String get(String requestId) {
    return stringRedisTemplate.opsForValue().get(requestId);
  }
}
