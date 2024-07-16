/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.captcha.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.openbytecode.starter.captcha.core.sms.ValidateCode;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 实现
 *
 * @author lijunping
 */
public class LocalCaptchaRepository implements CaptchaRepository{

    private final Cache<String, ValidateCode> cache;

    public LocalCaptchaRepository() {
        this.cache = Caffeine.newBuilder()
                .expireAfter(new CaffeineExpiry())
                .build();
    }


    @Override
    public <C extends ValidateCode> void save(String requestId, C code) {
        ValidateCode ValidateCode = new ValidateCode(code.getCode(), code.getExpireTime());
        cache.put(requestId, ValidateCode);
    }

    @Override
    public String get(String requestId) {
        ValidateCode code = cache.getIfPresent(requestId);
        if (code != null){
            return code.getCode();
        }
        return null;
    }

    static class CaffeineExpiry implements Expiry<String, ValidateCode> {
        @Override
        public long expireAfterCreate(@NonNull String key, @NonNull ValidateCode validateCode, long currentTime) {
            return TimeUnit.MINUTES.toNanos(validateCode.getExpireTime());
        }

        @Override
        public long expireAfterUpdate(@NonNull String key, @NonNull ValidateCode validateCode, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }

        @Override
        public long expireAfterRead(@NonNull String key, @NonNull ValidateCode validateCode, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }
    }
}
