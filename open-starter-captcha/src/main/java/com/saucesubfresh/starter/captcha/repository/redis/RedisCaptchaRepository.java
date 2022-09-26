/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.captcha.repository.redis;

import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import com.saucesubfresh.starter.captcha.repository.CaptchaRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis+deviceId的方式进行验证码的存、取、删
 *
 * @author lijunping
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
