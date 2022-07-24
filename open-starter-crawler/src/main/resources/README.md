# 爬虫插件

一个企业级的爬虫插件

## 功能

- [x] 支持 XPath、JsonPath、Css、Regex 四种爬取方式

- [x] 解析规则支持值传递和类注解两种方式

- [x] 爬虫过程采用 pipeline 方式实现，pipeline 支持自定义

- [x] 数据 id 生成支持自定义

- [x] 爬虫入参和出参支持自定义

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-crawler</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 配置参数

暂无

### 3. 解析规则值传递实例

节选自 Open-Crawler

```java

@Slf4j
@SpiderHandler(name = "autoCrawlerHandler")
public class AutoCrawlerSpiderHandler extends AbstractCrawlerSpiderHandler {

    private final List<Pipeline> pipelines;

    public AutoCrawlerSpiderHandler(DownloadPipeline downloadPipeline,
                                    ParserPipeline parserPipeline,
                                    FormatPipeline formatPipeline,
                                    ValuePipeline valuePipeline,
                                    FilterPipeline filterPipeline,
                                    SerializePipeline serializePipeline,
                                    PersistencePipeline persistencePipeline) {
        List<Pipeline> pipelines = new ArrayList<>();
        pipelines.add(downloadPipeline);
        pipelines.add(parserPipeline);
        pipelines.add(formatPipeline);
        pipelines.add(valuePipeline);
        pipelines.add(filterPipeline);
        pipelines.add(serializePipeline);
        pipelines.add(persistencePipeline);
        this.pipelines = pipelines;
    }

    @Override
    protected List<Pipeline> getPipeline() {
        return pipelines;
    }
}

```

### 4. 解析规则类注解实例

节选自 Open-Crawler

```java


```


## 1.0.0 版本

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

## 1.0.2 版本更新说明

采用固定的流程进行操作，此法存在弊端

虽然爬虫基本的操作有： 下载-> 解析-> 持久化，但是这三个操作又不是每个都是必须的，

例如可以简化为: 下载-> 持久化，中间简化了解析这一操作，

又例如别人想加一个操作流程: 下载-> 解析-> 计算-> 持久化

因此，我们使用类似流水线的处理方式，每条流水线中各个处理环节的顺序可以是不同的，数量也可以是不同的

系统默认的流水线是：下载-> 解析-> 格式化-> 值处理-> 持久化

## 注意事项

1. JsonPath 不可以与其他解析方法混用

2. 默认解析中如果所有字段都勾选了循环将进行转置，否则将不会转置

3. id 生成规则：如果指定了 id 字段，则指定的 id 字段将会被当作主键，

如果没有指定 id 字段而是在某些字段上勾选了主键则将会拼接这些字段的值进行散列生成一个 8 位的唯一字符串当作主键，

如果以上都没做，将使用第一个字段进行散列生成唯一id当作主键


## 免责声明

请勿将 open-starter-crawler 应用到任何可能会违反法律规定和道德约束的工作中,

请友善使用 open-starter-crawler，遵守蜘蛛协议，不要将 open-starter-crawler 用于任何非法用途。

如您选择使用 open-starter-crawler 即代表您遵守此协议，作者不承担任何由于您违反此协议带来任何的法律风险和损失，一切后果由您承担。