# 一款简单的基于 Redis 分布式限流插件

## 限流算法

### 固定窗口算法

使用固定窗口实现限流的思路大致为：将某一个时间段当做一个窗口，在这个窗口内存在一个计数器记录这个窗口接收请求的次数，

每接收一次请求便让这个计数器的值加一,如果计数器的值大于请求阈值的时候，即开始限流。 当这个时间段结束后，会初始化窗口的计数器数据，相当于重新开了一个窗口重新监控请求次数。

缺点：

固定窗口可以对一段时间的流量进行监控，但是有一个致命的问题，就是我们监控流量都是指时间段而不是具体的某一个时间，

假设我们的窗口时间为一分钟，当一分钟后会切换到另外一个窗口重新监控。这个切换就有可能出现问题。

假设我们在第一个窗口的最后十秒发了100个请求，在第二个窗口开始的时候也可以发100个请求，那么在这二十秒时间内服务器就会接收到200个请求，这样很有可能会使服务器接收的请求过多而挂掉。所以就出现了固定窗口的优化版，滑动窗口来解决这个问题。

### 滑动窗口算法

滑动窗口为固定窗口的改良版，解决了固定窗口在窗口切换时会受到两倍于阈值数量的请求，滑动窗口在固定窗口的基础上，

将一个窗口分为若干个等份的小窗口，每个小窗口对应不同的时间点，拥有独立的计数器，当请求的时间点大于当前窗口的最大时间点时，

则将窗口向前平移一个小窗口（将第一个小窗口的数据舍弃，第二个小窗口变成第一个小窗口，当前请求放在最后一个小窗口），整个窗口的所有请求数相加不能大于阀值。

### 漏桶算法

漏桶算法非常简单，就是将流量放入桶中并按照一定的速率流出。如果流量过大时候并不会提高流出效率，而溢出的流量也只能是抛弃掉了。

这种算法很简单，但也非常粗暴，无法应对突发的大流量。 这时可以考虑令牌桶算法。

### 令牌桶算法

![令牌桶算法-来自网络.gif](https://i.loli.net/2017/08/11/598c91f2a33af.gif)

令牌桶算法是按照恒定的速率向桶中放入令牌，每当请求经过时则消耗一个或多个令牌。当桶中的令牌为 0 时，请求则会被阻塞。

> note：
令牌桶算法支持先消费后付款，比如一个请求可以获取多个甚至全部的令牌，但是需要后面的请求付费。也就是说后面的请求需要等到桶中的令牌补齐之后才能继续获取。

## 拓展使用 Redis 限流

### 注入 RedisTemplate<String, Object> 及 RedisScript

```
@Bean
public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
    //StringRedisTemplate的构造方法中默认设置了stringSerializer
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    //set key serializer
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    template.setKeySerializer(stringRedisSerializer);
    template.setHashKeySerializer(stringRedisSerializer);

    //set value serializer
    template.setDefaultSerializer(jackson2JsonRedisSerializer());
    template.setConnectionFactory(lettuceConnectionFactory);
    template.afterPropertiesSet();
    return template;
}
```

```
@Bean
public RedisScript redisRateLimiterScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript<>();
    redisScript.setResultType(List.class);
    redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/rate_limiter.lua")));
    //redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
    return redisScript;
}
```

### 编写 lua 脚本

spring-cloud-gateway 最新

基于令牌桶算法实现

```lua
redis.replicate_commands()

local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]
--redis.log(redis.LOG_WARNING, "tokens_key " .. tokens_key)

local rate = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = redis.call('TIME')[1]
local requested = tonumber(ARGV[3])

local fill_time = capacity/rate
local ttl = math.floor(fill_time*2)

--redis.log(redis.LOG_WARNING, "rate " .. ARGV[1])
--redis.log(redis.LOG_WARNING, "capacity " .. ARGV[2])
--redis.log(redis.LOG_WARNING, "now " .. now)
--redis.log(redis.LOG_WARNING, "requested " .. ARGV[3])
--redis.log(redis.LOG_WARNING, "filltime " .. fill_time)
--redis.log(redis.LOG_WARNING, "ttl " .. ttl)

local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
  last_tokens = capacity
end
--redis.log(redis.LOG_WARNING, "last_tokens " .. last_tokens)

local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
  last_refreshed = 0
end
--redis.log(redis.LOG_WARNING, "last_refreshed " .. last_refreshed)

local delta = math.max(0, now-last_refreshed)
local filled_tokens = math.min(capacity, last_tokens+(delta*rate))
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
local allowed_num = 0
if allowed then
  new_tokens = filled_tokens - requested
  allowed_num = 1
end

--redis.log(redis.LOG_WARNING, "delta " .. delta)
--redis.log(redis.LOG_WARNING, "filled_tokens " .. filled_tokens)
--redis.log(redis.LOG_WARNING, "allowed_num " .. allowed_num)
--redis.log(redis.LOG_WARNING, "new_tokens " .. new_tokens)

if ttl > 0 then
  redis.call("setex", tokens_key, ttl, new_tokens)
  redis.call("setex", timestamp_key, ttl, now)
end

-- return { allowed_num, new_tokens, capacity, filled_tokens, requested, new_tokens }
return { allowed_num, new_tokens }
```

令牌桶的关键是以下几个参数:

令牌流入速率

桶容量

每次请求许可数