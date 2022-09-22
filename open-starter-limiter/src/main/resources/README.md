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
 
注入 RedisRateLimiter

```

@Bean
public RateLimiter rateLimiter(RedisScript script, RedisTemplate<String, Object> redisTemplate){
    return new RedisRateLimiter(script, redisTemplate);
}
```

lua 脚本 参考 spring-cloud-gateway 最新版本，基于令牌桶算法实现

令牌桶的关键是以下几个参数: 令牌流入速率、桶容量 、每次请求许可数

## tips

1. 如果是单机的，建议使用 Guava 的 RateLimiter

2. 如果本插件的限流时间单位不满足你的需求，建议直接使用 Redisson 的 RRateLimiter

## 版本升级说明

### 1.0.2 版本更新说明

1.0.2 为第一个版本





