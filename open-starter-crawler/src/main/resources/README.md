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

因此，我们使用类似流水线的处理方式，每条流水线中各个处理环节的顺序可以是不同的，数量也可以是不同的

系统默认的流水线是：下载-> 解析-> 格式化-> 值处理-> 持久化

# 注意事项

注意点

1. jsonPath 不可以与其他解析方法混用

2. 默认解析中如果所有字段都勾选了循环将进行转置，否则将不会转置

3. id 生成规则：如果指定了 id 字段，则指定的 id 字段将会被当作主键，如果没有指定 id 字段而是在某些字段上勾选了主键
   则将会拼接这些字段的值进行散列生成一个 8 位的唯一字符串当作主键，如果以上都没做，将使用第一个字段进行散列生成唯一id当作主键
