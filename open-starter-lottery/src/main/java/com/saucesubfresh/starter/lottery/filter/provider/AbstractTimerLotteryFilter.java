package com.saucesubfresh.starter.lottery.filter.provider;

import com.saucesubfresh.starter.lottery.domain.LotteryRequest;
import com.saucesubfresh.starter.lottery.filter.LotteryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Objects;

/**
 * @author lijunping on 2022/1/4
 */
public abstract class AbstractTimerLotteryFilter<Req extends LotteryRequest> implements LotteryFilter<Req> {

    @Autowired
    private DefaultRedisScript<Boolean> defaultRedisScript;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    protected boolean executeLuaScript(String key, String initNum, String totalNum, String expireTime) {
        Object res = redisTemplate.execute((RedisConnection connection) ->
                connection.eval(
                        defaultRedisScript.getScriptAsString().getBytes(),
                        ReturnType.BOOLEAN,
                        1,
                        key.getBytes(),
                        initNum.getBytes(), totalNum.getBytes(), expireTime.getBytes()));

        if (Objects.nonNull(res)) {
            return (Boolean) res;
        }
        return false;
    }
}
