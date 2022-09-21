package com.saucesubfresh.starter.lock.processor;

import com.saucesubfresh.starter.lock.exception.LockException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author lijunping on 2022/8/25
 */
public class RedisDistributedLockProcessor implements DistributedLockProcessor{

    private final RedisScript<List<Long>> script;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisDistributedLockProcessor(RedisScript<List<Long>> script,
                                         RedisTemplate<String, Object> redisTemplate) {
        this.script = script;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> T lock(Supplier<T> callback, String lockName, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        throw new LockException("Not support exception");
    }

    @Override
    public <T> T tryLock(Supplier<T> callback, String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) throws Exception {
        long threadId = Thread.currentThread().getId();
        try {
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockName, threadId, leaseTime, timeUnit);
            if (Boolean.TRUE.equals(acquired)){
                return callback.get();
            }
        }finally {
            deleteLock(lockName, threadId);
        }
        return null;
    }


    private void deleteLock(String lockName, long value){
        this.redisTemplate.execute(this.script, Collections.singletonList(lockName), value);
    }
}
