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
package com.openbytecode.starter.lock.processor;

import com.openbytecode.starter.lock.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * id 的作用：防止多个实例，线程 id 相同锁重入
 *
 * @author lijunping
 */
@Slf4j
public class RedisDistributedLockProcessor implements DistributedLockProcessor{

    private final String id;
    private final RedisScript<Boolean> lockScript;
    private final RedisScript<Long> unlockScript;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisDistributedLockProcessor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/redis_lock.lua")));
        lockScript.setResultType(Boolean.class);
        DefaultRedisScript<Long> unlockScript = new DefaultRedisScript<>();
        unlockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/redis_unlock.lua")));
        unlockScript.setResultType(Long.class);
        this.lockScript = lockScript;
        this.unlockScript = unlockScript;
        id = UUID.randomUUID().toString();
    }

    @Override
    public <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        throw new LockException("Not support exception");
    }

    @Override
    public <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        long threadId = Thread.currentThread().getId();
        String filedKey = id + ":" + threadId;
        try {
            if (tryAcquireLock(lockName, filedKey, timeUnit.toMillis(leaseTime))){
                return callback.get();
            }
            return null;
        }finally {
            unlock(lockName, filedKey);
        }
    }

    /**
     * 加锁
     * @param lockName 锁名称，hashKey
     * @param filedKey filedKey
     * @param leaseTime 锁超时时间
     * @return true 加锁成功，false 加锁失败
     */
    private boolean tryAcquireLock(String lockName, String filedKey, long leaseTime){
        Boolean result = this.redisTemplate.execute(this.lockScript, Collections.singletonList(lockName), leaseTime, filedKey);
        return Boolean.TRUE.equals(result);
    }

    /**
     * 解锁
     * @param lockName 锁名称，hashKey
     * @param filedKey filedKey
     */
    private void unlock(String lockName, String filedKey){
        this.redisTemplate.execute(this.unlockScript, Collections.singletonList(lockName), filedKey);
    }
}
