# 任务执行插件

## 功能

-[x] 支持同一任务使用同一线程执行，不同任务之间并行调度、并行执行，同一任务排队有序执行

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-executor</artifactId>
    <version>1.0.5</version>
</dependency>
```

### 2. 配置执行器相关参数

```yaml
com:
  saucesubfresh:
    executor:
      timeout: 3 #超时时间
```

### 3. 使用案例

节选自 Open-Job

```java JobMessageProcessor.java
private void runTask(JobThreadQueueNode jobThreadQueueNode){
    taskExecutor.execute(jobThreadQueueNode, ((queueNode, isStop) -> {
        if (isStop){
            ResponseWriter responseWriter = queueNode.getResponseWriter();
            MessageResponseBody responseBody = queueNode.getResponseBody();
            responseBody.setMsg("job not executed in the job queue, killed.");
            responseBody.setStatus(ResponseStatus.ERROR);
            responseWriter.write(responseBody);
        }else {
            execute(queueNode);
        }
    }));
}
```
