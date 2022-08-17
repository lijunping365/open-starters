# 前言

一个简单封装了 httpClient 和 okHttp 的工具，只支持 application/json 请求

## 功能

- [x] 对 httpClient 和 okHttp 进行了简单封装，使用方便

- [x] 目前只支持 Get、Post 请求方式，且是 application/json 请求

- [x] client 超时时间、线程池大小支持自定义配置

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-http</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 2. 配置相关参数

```yaml
com:
  saucesubfresh:
    http:
      # 存活时长，单位 min
      keep-alive: 5
      # 连接超时时间，单位毫秒
      time-out: 5000
      # 某一个服务每次能并行接收的请求数量。
      max-per-route: 500
      # 连接池数量
      pool-size: 200
```

### 3. 使用示例

节选自 Open-Crawler

```java

@Slf4j
@Component
@AllArgsConstructor
public class CustomizeDownloadPipeline extends AbstractDownloadPipeline {

    private final OkHttpExecutor httpExecutor;

    @Override
    protected void doDownload(SpiderRequest request, SpiderResponse response) throws CrawlerException {
        ISpiderRequest spiderRequest = (ISpiderRequest) request;
        CustomHttpRequest httpRequest = buildHttpRequest(spiderRequest);
        try {
            String result = httpExecutor.execute(httpRequest);
            response.setBody(result);
        } catch (HttpException e) {
            log.error("Download error {}", e.getMessage());
            throw new CrawlerException(e.getMessage());
        }
    }

    protected CustomHttpRequest buildHttpRequest(ISpiderRequest request){
        CustomHttpRequest httpRequest = new CustomHttpRequest();
        httpRequest.setUrl(request.getUrl());
        String method = request.getMethod();
        httpRequest.setMethod(StringUtils.isNotBlank(method) ? method : HttpConstant.Method.GET.name());
        httpRequest.setParams(request.getParams());
        httpRequest.setData(request.getData());
        Map<String, String> headers = request.getHeaders();
        if (CollectionUtils.isEmpty(headers)){
            headers = new HashMap<>();
        }
        headers.putIfAbsent(HttpConstant.Header.CONTENT_TYPE, HttpConstant.ContentType.JSON);
        headers.putIfAbsent(HttpConstant.Header.USER_AGENT, HttpConstant.UserAgent.USER_AGENT_CHROME);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }
}
```

## 版本升级说明

### 1.0.1 版本更新说明

1. 替换 httpClient post form 表单请求为 application/json 请求

### 1.0.2 版本更新说明

1. OkHttp 去掉 retryOnConnectionFailure 属性

2. 优化注入方式以及配置，默认注入的是 OkHttpExecutor

### 1.0.3 版本更新说明

1. OkHttp post form 表单请求为 application/json 请求

### 1.0.4 版本更新说明

1. post 请求添加 data 字段，用来承载 post 请求体