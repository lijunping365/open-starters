# 爬虫插件

一个企业级的爬虫插件

## 功能

- [x] 支持 XPath、JsonPath、Css、Regex 四种爬取方式

- [x] 解析规则支持值传递和类注解两种方式

- [x] 包含爬虫核心流程

- [x] 数据 id 生成支持自定义

- [x] 爬虫入参和出参支持自定义

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-crawler</artifactId>
    <version>1.0.6</version>
</dependency>
```

### 2. 配置参数

暂无

## 1.0.0 版本说明

采用固定流程进行操作：下载-> 解析-> 持久化

```java
@Slf4j
public class DefaultSpiderExecutor implements SpiderExecutor{

    @Override
    public void execute(List<SpiderRequest> requests, SpiderParser spiderParser, Pipeline pipeline) {
        requests.foreach(request->{
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

采用固定的流程进行操作，此法存在弊端，无法满足开闭原则，如果比如如果在下载后需要

虽然爬虫基本的操作有： 下载-> 解析-> 持久化，但是这三个操作又不是每个都是必须的，

例如可以简化为: 下载-> 持久化，中间简化了解析这一操作，

又例如别人想加一个操作流程: 下载-> 解析-> 计算-> 持久化

因此，我们使用类似流水线的处理方式，每条流水线中各个处理环节的顺序可以是不同的，数量也可以是不同的

系统默认的流水线是：下载-> 解析-> 格式化-> 值处理-> 持久化

系统提供较多默认流水线操作实现供用户使用

## 1.0.3 版本更新说明

爬虫执行流程简化，只包含爬虫的核心流程：下载和解析，下载由用户自己实现，满足用户不同的需求，当然也可以使用我们的 http 插件，解析后数据交给用户，供用户处理。

## 1.0.4 版本更新说明

1. 支持在爬虫主流程上增加拦截器

2. 优化值创建时间的设置

## 1.0.5 版本更新说明

列表转置优化及注解增强

## 1.0.6 版本更新说明

爬虫执行流程重构，在爬虫执行链上添加其他流程更加简单、方便，更容易拓展，相信这是一个好的功能更新。

## 注意事项

1. JsonPath 不可以与其他解析方法混用

2. id 生成规则：如果指定了 id 字段，则指定的 id 字段将会被当作主键，

如果没有指定 id 字段而是在某些字段上勾选了主键则将会拼接这些字段的值进行散列生成一个 8 位的唯一字符串当作主键，

如果以上都没做，将使用第一个字段进行散列生成唯一id当作主键


## 免责声明

请勿将 open-starter-crawler 应用到任何可能会违反法律规定和道德约束的工作中,

请友善使用 open-starter-crawler，遵守蜘蛛协议，不要将 open-starter-crawler 用于任何非法用途。

如您选择使用 open-starter-crawler 即代表您遵守此协议，作者不承担任何由于您违反此协议带来任何的法律风险和损失，一切后果由您承担。