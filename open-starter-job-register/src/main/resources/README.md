# open-starter-job-register

Open-Job 之 jobHandler 扫描注册插件

## 功能

- [x] 基于注解扫描

- [x] 支持方法级别的 jobHandler

- [x] 同时还支持类级别的 jobHandler

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-job-register</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 2. 配置参数

暂无

### 3. 使用示例

节选自 Open-Job

例一：

```java
@Slf4j
@JobHandler(name = "job-one")
@Component
public class OpenJobHandlerOne implements OpenJobHandler {

    @Override
    public void handler(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务");
    }
}

```

例二：

```java
@Slf4j
@Component
public class OpenJobHandlerMethodOne{

    @JobHandler(name = "job-method-one1")
    public void handlerOne1(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务, 任务参数 {}", jobParam.getParams());
    }

    @JobHandler(name = "job-method-one2")
    public void handlerOne2(JobParam jobParam) {
        log.info("JobHandlerOne 处理任务, 任务参数 {}", jobParam.getParams());
    }
}
```

## 版本更新说明

### 1.0.2 

第一个版本

### 1.0.3

执行接口增加 jobId 参数，script 参数

### 1.0.4

执行接口增加分片参数

### 1.0.5

增加 JobHandler 管理器

### 1.0.6

JobHandler 接口支持泛型，这样 handler 方法的参数类型可由外部自定义
