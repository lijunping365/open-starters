# 前言

一款简单的基于 Redis 实现的分布式限流插件

## 功能

- [x] 提供注解限流功能

- [x] 默认提供了基于令牌桶算法实现的 Redisson RRateLimiter，支持自定义 lua 脚本实现限流算法

- [x] 支持自定义 lua 脚本，实现自定义的限流算法

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-limit</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 配置相关参数

```yaml

```

### 3. 使用示例

节选自 Open-Crawler

```java

@Service
public class LimitServiceImpl implements LimitService {

    @Override
    @RateLimit(prefix = "test")
    public String rateLimit(int id){
        return "执行了" + id;
    }

}
```

对于限流异常捕获，建议使用该方式进行全局异常捕获处理

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({LimitException.class})
  public Result<Object> limitException(LimitException ex) {
    log.warn("[limitException]", ex);
    return Result.failed(666, ex.getMessage());
  }
}
```

## 拓展使用 Redis 限流

### 注入 RedisTemplate<String, Object> 、 RedisScript 、 RedisRateLimiter

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

@Bean
public RateLimiter rateLimiter(RedisScript script, RedisTemplate<String, Object> redisTemplate){
    return new RedisRateLimiter(script, redisTemplate);
}
```

### 编写 lua 脚本

参考 spring-cloud-gateway 最新版本，基于令牌桶算法实现

令牌桶的关键是以下几个参数:令牌流入速率、桶容量 、每次请求许可数

在 resources 目录下创建 scripts 文件夹，在 scripts 文件夹下创建 rate_limiter.lua 文件

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

## tips

1. 如果是单机的，建议使用 Guava 的 RateLimiter

2. 如果本插件的限流时间单位不满足你的需求，建议直接使用 Redisson 的 RRateLimiter

## 版本升级说明

### 1.0.2 版本更新说明

1.0.2 为第一个版本





