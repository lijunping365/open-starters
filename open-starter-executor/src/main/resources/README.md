# 任务执行器插件

## 功能

- [x] 对进入线程池工作的线程在执行任务时进行拦截，可在线程执行前后完成指定的功能

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-executor</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 2. 配置验证码相关参数

```yaml
com:
  saucesubfresh:
    executor:
      core-pool-size: 10 #核心线程数
```

### 3. 使用案例

节选自 Open-Crawler

```java
@Slf4j
@Component
public class DefaultCrawlerExecutor implements CrawlerExecutor{
    
    private final TaskProcessor<ISpiderRequest> taskProcessor;
    public DefaultCrawlerExecutor(TaskProcessor<ISpiderRequest> taskProcessor) {
        this.taskProcessor = taskProcessor;
    }

    @Override
    public void executeAsync(List<ISpiderRequest> requests) {
        taskProcessor.execute(requests, (task -> {
            ISpiderResponse response = new ISpiderResponse();
            response.setSerializeClass(CrawlerData.class);
            this.doExecute(runPipelines, (SpiderRequest) task, response);
        }));
    }

    private void doExecute(List<Pipeline> pipelines, SpiderRequest request, SpiderResponse response){
        pipelines.forEach(pipeline -> pipeline.process(request, response));
    }
}
```

## 1.0.1 版本更新说明

1. 去掉了发送回调接口

2. 对验证码生成及验证整体进行了重构和优化

3. 加入了数学表达式图形验证码功能

4. 配置优化，使用了 @NestedConfigurationProperty，配置时有提示

# 任务执行器插件

对进入线程池工作的线程在执行任务时进行拦截

[x] 对线程池中的线程在执行时进行拦截，可在线程执行前后完成指定的功能

由于我们在实际项目中使用**线程池执行器**的情况比较多，所以这里我们提供一个线程池执行器。

### 任务执行前后拦截器：

[@see TaskBeforeInterceptor，TaskAfterInterceptor]

我们利用 TaskBeforeInterceptor #before 完成了任务的装饰，
我们利用 TaskAfterInterceptor #after 完成了任务执行后的处理
类似于 js 中的拦截器，有 requestInterceptor 和 responseInterceptor，提供钩子函数，供开发者自己实现。

### 任务执行成功失败处理器：

[@see TaskExecuteSuccessHandler，TaskExecuteFailureHandler]

我们利用 TaskExecuteSuccessHandler #onTaskExecuteSuccess 完成任务执行成功后的处理逻辑，提供钩子函数，供开发者自己实现。
我们利用 TaskExecuteFailureHandler #onTaskExecuteFailure 完成任务执行失败后的处理逻辑，提供钩子函数，供开发者自己实现。
类似于 oauth2 中的认证成功失败处理器，有 onAuthenticationSuccess 和 onAuthenticationFailure，提供钩子函数，供开发者自己实现。




# 已实现功能

(1)动态调参：支持线程池参数动态调整、界面化操作；包括修改线程池核心大小、最大核心大小、队列长度等；参数修改后及时生效。
单机版容易实现，集群版写过已删除，较为麻烦。

(2)线程池、队列、拒绝策略支持自定义

# 后续优化点

(3)任务监控：支持应用粒度、线程池粒度、任务粒度的Transaction监控；可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间。

(4)队列支持自动扩容，默认队列大小，扩容因子，但是自动扩容需要重点注意一点，要考虑到 cpu 使用率，不能超过 cpu 使用率的 80%；