# 1.引言

在现在互联网系统中，随着用户量的增长，单数据源通常无法满足系统的负载要求。因此为了解决用户量增长带来的压力，在数据库层面会采用读写分离技术和数据库拆分等技术。

读写分离就是就是一个Master数据库，多个Slave数据库，Master数据库负责数据的写操作，slave库负责数据读操作，通过slave库来降低Master库的负载。

因为在实际的应用中，数据库都是读多写少（读取数据的频率高，更新数据的频率相对较少），而读取数据通常耗时比较长，占用数据库服务器的CPU较多，从而影响用户体验。

我们通常的做法就是把查询从主库中抽取出来，采用多个从库，使用负载均衡，减轻每个从库的查询压力。

同时随着业务的增长，会对数据库进行拆分，根据业务将业务相关的数据库表拆分到不同的数据库中。不管是读写分离还是数据库拆分都是解决数据库压力的主要方式之一。

多数据源实现读写分离

# 2.概念讲解

## 连接池概述

（1）HiKariCP，CP即Connection Pool，代码经过精简优化，jar包体积很小，稳定可靠、性能极高，号称性能最高的连接池，是springboot 2.x默认的数据库连接池。hikari的高性能得益于最大限度地避免锁竞争。
    springboot 2.x默认使用hikari作为数据库连接池，自带了hikari的依赖，不需要添加额外依赖。
    
（2）druid是阿里开源的连接池，性能略低于hikari，但功能全面、扩展性强，对数据库操作有监控、统计功能，便于分析、优化数据库操作。

### HiKari yml 配置样例

yml

```yaml
spring:
  datasource:
    #默认就是hikari，可缺省
    #type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://localhost:3306/mall?serverTimezone=UTC
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: abcd
    hikari:
      #默认30000 ms，即30s
      connection-timeout: 30000
      #默认600000 ms，即10min
      idle-timeout: 600000
      #连接池中的最大连接数（active+idle），默认10
      maximum-pool-size: 200
      #默认10
      minimum-idle: 50
```


### Druid yml 配置样例

```yaml
spring:
  datasource:
    # 数据源基本配置
    type: com.alibaba.druid.pool.DruidDataSource
    username: root
    # ljp2521483363 / root /
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/fresh_sys?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT&allowPublicKeyRetrieval=true
    # 数据源其他配置
    # 配置初始化大小/最小/最大
    initialSize: 5
    minIdle: 5
    maxActive: 20
    # 获取连接等待超时时间
    maxWait: 60000
    # 间隔多久进行一次检测，检测需要关闭的空闲连接
    timeBetweenEvictionRunsMillis: 60000
    # 一个连接在池中最小生存的时间
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 'x'
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    # 打开PSCache，并指定每个连接上PSCache的大小。oracle设为true，mysql设为false。分库分表较多推荐设置为false
    poolPreparedStatements: false
    # 监控统计拦截的filters
    filters: stat
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500

```
### 配置注意点

1. mysql 5.x driver-class-name: com.mysql.jdbc.Driver    mysql-connector-java 5.1.34

2. mysql 8.x driver-class-name: com.mysql.cj.jdbc.Driver mysql-connector-java 8.0.22

#3. 实现功能

（1）多数据源注解启用功能，如果主启动类有该注解则启用，没有不启用多数据源

（2）如果启用了多数据源功能，提供注解选择对应的数据源

#4. 多数据源实现流程

## 1. 使用 HiKari

### 1. 配置 yml

```yaml
spring:
  datasource:
    hikari:
      portfolio:
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://192.168.1.138:3306/portfolio_account?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
        username: root
        password: "!@#456QWEasd"
        type: com.zaxxer.hikari.HikariDataSource
        maximum-pool-size: 15
        connection-timeout: 60000
        minimum-idle: 10
      finance:
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://192.168.1.138:3306/finance?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
        username: root
        password: "!@#456QWEasd"
        type: com.zaxxer.hikari.HikariDataSource
        maximum-pool-size: 15
        connection-timeout: 60000
        minimum-idle: 10
```

### 2. 编写配置类

```
@Bean
@ConfigurationProperties(prefix = "spring.datasource.hikari.portfolio")
public DataSource portfolio(){
    return DataSourceBuilder.create().build();
}

@Bean
@ConfigurationProperties(prefix = "spring.datasource.hikari.finance")
public DataSource finance(){
    return DataSourceBuilder.create().build();
}
```

## 2. 使用 Druid

### 1. 配置 yml

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      master:
        # 主数据源基本配置
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://39.105.51.99:3306/fresh_sys?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT&allowPublicKeyRetrieval=true
        # ljp2521483363 / root /
        username: root
        password:
        # 数据源其他配置
        # 配置初始化大小/最小/最大
        initialSize: 5
        minIdle: 5
        maxActive: 20
        # 获取连接等待超时时间
        maxWait: 60000
        # 间隔多久进行一次检测，检测需要关闭的空闲连接
        timeBetweenEvictionRunsMillis: 60000
        # 一个连接在池中最小生存的时间
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并指定每个连接上PSCache的大小。oracle设为true，mysql设为false。分库分表较多推荐设置为false
        poolPreparedStatements: false
        # 监控统计拦截的filters
        filters: stat
        maxPoolPreparedStatementPerConnectionSize: 20
        useGlobalDataSourceStat: true
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500

      slave:
        # 从数据源基本配置
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://39.105.51.99:3306/fresh_sys?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=CTT&allowPublicKeyRetrieval=true
        # ljp2521483363 / root /
        username: root
        password:
        # 数据源其他配置
        # 配置初始化大小/最小/最大
        initialSize: 5
        minIdle: 5
        maxActive: 20
        # 获取连接等待超时时间
        maxWait: 60000
        # 间隔多久进行一次检测，检测需要关闭的空闲连接
        timeBetweenEvictionRunsMillis: 60000
        # 一个连接在池中最小生存的时间
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        # 打开PSCache，并指定每个连接上PSCache的大小。oracle设为true，mysql设为false。分库分表较多推荐设置为false
        poolPreparedStatements: false
        # 监控统计拦截的filters
        filters: stat
        maxPoolPreparedStatementPerConnectionSize: 20
        useGlobalDataSourceStat: true
        connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
```

### 2. 编写配置类
这里注意如果是使用 Druid 需要使用 DruidDataSourceBuilder ，不然数据源还是hikari
```
@Bean
@ConfigurationProperties(prefix="spring.datasource.druid.master")
public DataSource masterDataSource(){
    return DruidDataSourceBuilder.create().build();
}

@Bean
@ConfigurationProperties(prefix="spring.datasource.druid.slave")
public DataSource slaveDataSource(){
    return DruidDataSourceBuilder.create().build();
}
```

其他

```java
@Component
public class DataSourceManager {

    private final DataSourceTransactionManager dataSource;

    @Autowired
    public DataSourceManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSource = dataSourceTransactionManager;
    }

    public TransactionStatus begin(){
        return dataSource.getTransaction(new DefaultTransactionDefinition());
    }

    public void commit(TransactionStatus transactionStatus){
        dataSource.commit(transactionStatus);
    }

    public void rollback(TransactionStatus transactionStatus){
        dataSource.rollback(transactionStatus);
    }
}
```

```
TransactionStatus status = dataSourceManager.begin();
Boolean success = Boolean.TRUE;
try {
    int close = portfolioHistoryDao.insertPortfolioHistory(closePosition);
    if(close <= 0){
        success = Boolean.FALSE;
    }
    if(diff.compareTo(BigDecimal.ZERO) == 0){
        int delete = portfolioPositionDao.deletePortfolioPositionById(
                portfolioPositionCloseParam.getPositionId(),
                portfolioPositionCloseParam.getPortfolioAccountId()
        );
        if(delete <= 0){
            success = Boolean.FALSE;
            LOGGER.warn("操作平仓 delete position 失败 "
                    + " accountId:" + portfolioPositionCloseParam.getPortfolioAccountId()
                    + " positionId:" + portfolioPositionCloseParam.getPositionId()
                    + " number:" + diff);
        }
    }else{
        int update = portfolioPositionDao.updatePortfolioNumber(
                portfolioPositionCloseParam.getPositionId(),
                diff,
                portfolioPositionCloseParam.getPortfolioAccountId(),
                cost
        );
        if(update <= 0){
            success = Boolean.FALSE;
            LOGGER.warn("操作平仓 update portfolioNumber 失败 "
                    + " accountId:" + portfolioPositionCloseParam.getPortfolioAccountId()
                    + " positionId:" + portfolioPositionCloseParam.getPositionId()
                    + " number:" + diff);
        }
    }
    if(!success){
        dataSourceManager.rollback(status);
    }else{
        dataSourceManager.commit(status);
        vo.setSuccess(Boolean.TRUE);
        userOperationService.operationPortfolioPosition(portfolioPositionCloseParam.getPortfolioAccountId());
        return vo;
    }
}catch (Exception e){
    dataSourceManager.rollback(status);
    LOGGER.error("操作平仓异常 positionId:" + portfolioPositionCloseParam.getPositionId()
            + " accountId:" + portfolioPositionCloseParam.getPortfolioAccountId()
            + " error:" + e.getMessage(),e);
}
```

