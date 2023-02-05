# 分布式缓存组件

## 分布式缓存介绍

**为什么需要二级缓存**：

就在想如果一级缓存里面有数据，还要二级缓存干啥？ 首先为什么要用缓存？

不就是因为我们数据库压力太大了嘛，查询太慢了嘛，好办！我们给他加个缓存，为了尽量减轻服务器内存的压力，我们可以先加一个 redis 缓存，后来发现请求全跑到 redis 上了，

redis 压力也很大， redis 也快扛不住了，我们为了减轻 redis 的压力，这个时候没办法了，我们必须要加一个本地缓存，占用服务器的一部分内存来做缓存，来减轻 redis 的压力，即一部分数据走本地缓存，一部分数据走 redis 缓存

一级缓存是应用内缓存，二级缓存是比一级缓存更大的一个容器，它是应用集群获取缓存数据的统一载体。

二级缓存存在的意义就是如果二级缓存中有数据，集群中任一节点在本地缓存中没获取到数据的时候可以从二级缓存中直接获取到数据而不用查询数据库。

**查询数据的流程**：

进程内缓存做为一级缓存，分布式缓存做为二级缓存，首先从一级缓存中查询，若能查询到数据则直接返回， 

否则从二级缓存中查询，若二级缓存中可以查询到数据，则回填到一级缓存中，并返回数据。 

若二级缓存也查询不到，则从数据源中查询，将结果分别回填到一级缓存，二级缓存中。


## 说明

如果你只需要一级缓存，那这个组件可能并不适合，如果你需要一个既有分布式缓存能力还能通过api来操作缓存而且还能查看缓存的命中率等指标数据，那这款缓存组件就非常适合你了

## 功能

- [x] 基于 spring aop 实现，二级缓存模式

- [x] 系统默认提供了两种缓存实现方式，caffeine + redis 和 caffeine + redisson，支持用户自定义

- [x] 缓存配置参数 maxIdleTime，maxSize、ttl 支持 yaml 和 json 两种配置方式，且只针对本地缓存生效

- [x] 缓存 key 生成策略支持用户自定义

- [x] 本地缓存同步提供三种实现方式，分别是基于 redis 的 MQ、redisson 的 RTopic、Kafka，支持用户自定义

- [x] 支持缓存预热，即同步二级缓存数据到本地缓存中

- [x] 提供对外操作缓存的api，用户可通过该api进行缓存的清除，更新等操作

- [x] 该插件默认会存储空值，无需配置

- [x] 提供缓存的命中率指标查询，并自带缓存命中率等指标数据上报功能，是否上报即上报频率可配置，上报方式需用户自行实现

- [x] 支持多应用使用，不同的应用需配置不同的 namespace，相当于应用名称

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-cache</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 分布式缓存相关参数

```yaml
com:
  saucesubfresh:
    cache:
      # 一级缓存配置文件地址
      config-location: classpath:/open-cache-config.yaml
      # 应用名称
      namespace: applicationName
      # 实例 id
      instance-id: 127.0.0.1:8080
      # 是否开启自动缓存指标信息上报
      enable-metrics-report: true
      # 自动上报周期，默认 60，单位秒
      metrics-report-cycle: 60
      # 键值输入的最大空闲时间(毫秒)。
      max-idle-time: 720000
      # 缓存容量（缓存数量最大值）
      max-size: 100
      # 键值条目的存活时间，以毫秒为单位。
      ttl: 30000
```

### 3. OpenCacheable

使用缓存，节选自 Open-Cache

```java
@Service
public class UserServiceImpl implements UserService {

    @OpenCacheable(cacheName = "test", key = "#id")
    @Override
    public UserDO loadUserById(Long id) {
        UserDO userDO = new UserDO();
        userDO.setId(18L);
        userDO.setName("lijunping");
        return userDO;
    }
}
```

## 扩展示例

### 1. 自定义缓存实现方式（ClusterCache + CacheManager）

比如要使用 Ehcache + Redis

```java
public class RedisEhcacheCache extends AbstractClusterCache {
    
}

@Component
public class RedisEhcacheManager extends AbstractCacheManager {
    private final CacheProperties properties;
    private final CacheMessageProducer producer;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisEhcacheManager(CacheProperties properties,
                               ConfigFactory configFactory,
                               CacheMessageProducer producer,
                               RedisTemplate<String, Object> redisTemplate) {
        super(configFactory);
        this.properties = properties;
        this.producer = producer;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        return new RedisEhcacheCache(cacheName, namespace, cacheConfig, producer, redisTemplate);
    }
}
```


### 2. 自定义缓存 key 生成方式（KeyGenerator）

```java
@Component
public class CustomerKeyGenerator implements KeyGenerator{
    
    @Override
    public String generate(String cacheKey, Method method, Object[] args) {
        return null;
    }
}
```

### 3. 自定义本地缓存同步实现方式（CacheMessageProducer，CacheMessageListener）

```java
@Component
public class RocketMqCacheMessageProducer extends AbstractCacheMessageProducer{

    @Override
    public void broadcastCacheMessage(CacheMessage message) {
        
    }
}

@Component
public class RocketMqCacheMessageListener extends AbstractCacheMessageListener {
    
}
```

## 1.0.2 版本更新说明

1.0.2 是该组件的第一个版本，包含该组件的全部功能。

## 1.0.3 版本更新说明

1. 修复了重复广播同步消息的 bug。

2. 优化发送缓存同步消息，提供异常处理器。

3. 优化监听同步消息执行异常，在执行时抛出异常，在监听处捕获异常，用异常处理器去处理，这样可以区分是监听消息异常还是远程调用异常。

4. 修改实例id为机器标识

5. 增加统计 cacheKey 数量方法
