# 使用方法

v1.0.0 

采用固定流程进行操作：下载-> 解析-> 持久化

```java
@Slf4j
public class DefaultSpiderExecutor implements SpiderExecutor{

    private final TaskProcessor<SpiderRequest> taskProcessor;
    private final SpiderSuccessHandler spiderSuccessHandler;
    private final SpiderFailureHandler spiderFailureHandler;
    private final SpiderDownload spiderDownload;

    public DefaultSpiderExecutor(TaskProcessor<SpiderRequest> taskProcessor, SpiderSuccessHandler spiderSuccessHandler, SpiderFailureHandler spiderFailureHandler, SpiderDownload spiderDownload) {
        this.taskProcessor = taskProcessor;
        this.spiderSuccessHandler = spiderSuccessHandler;
        this.spiderFailureHandler = spiderFailureHandler;
        this.spiderDownload = spiderDownload;
    }

    @Override
    public void execute(List<SpiderRequest> requests, SpiderParser spiderParser, Pipeline pipeline) {
        taskProcessor.execute(requests, task -> {
            SpiderRequest request = (SpiderRequest) task;
            try {
                // 1.download
                SpiderResponse response = spiderDownload.download(request);
                // 2.parser
                spiderParser.process(request, response);
                // 3.pipeline
                pipeline.process(request, response);
                onSuccess(request, response);
            } catch (SpiderException e) {
                onError(request, e);
            }
        });
    }

    protected void onSuccess(SpiderRequest request, SpiderResponse response) {
        spiderSuccessHandler.onSuccess(request, response);
    }

    private void onError(SpiderRequest request, SpiderException e) {
        spiderFailureHandler.onError(request, e);
    }
}
```

v1.0.1

采用固定的流程进行操作，此法存在弊端

虽然爬虫基本的操作有： 下载-> 解析-> 持久化，但是这三个操作又不是每个都是必须的，

例如可以简化为: 下载-> 持久化，中间简化了解析这一操作，

又例如别人想加一个操作流程: 下载-> 解析-> 计算-> 持久化

所以我们可以采用类似 netty 中 ChannelHandler 处理方式来实现


