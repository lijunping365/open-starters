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
    <version>1.0.3</version>
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

注入 RedisDistributedLockProcessor

```java
@Configuration
@AutoConfigureBefore({RedisTemplate.class, RedisAutoConfiguration.class})
public class RedisConfig {

    @Bean
    public DistributedLockProcessor distributedLockProcessor(RedisTemplate<String, Object> redisTemplate){
        return new RedisDistributedLockProcessor(redisTemplate);
    }
}
```

## 版本更新说明

1.0.2

支持 redisson

1.0.3

支持 redis + lua 方式





