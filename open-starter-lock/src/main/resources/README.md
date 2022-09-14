# 前言

一款简单的基于 Redisson 实现的分布式锁插件

## 功能

- [x] 提供注解加锁功能

- [x] 默认提供了 Redisson RLock

- [x] 支持公平锁和非公平锁

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-lock</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 配置相关参数

```yaml

```

### 3. 使用示例

节选自 Open-Crawler

```java


```

## 拓展使用 Redis 分布式锁

### 注入 RedisTemplate<String, Object> 、 RedisScript 、 RedisLock

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
public RedisScript redisLockScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript<>();
    redisScript.setResultType(List.class);
    redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/rate_limiter.lua")));
    //redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
    return redisScript;
}

```

### 编写 lua 脚本


## 版本升级说明

### 1.0.2 版本更新说明

1.0.2 为第一个版本





