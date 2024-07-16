# ChatGPT的Java客户端

## 功能
- [x] 支持查询模型列表
- [x] 支持查询模型详细信息
- [x] 支持最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
- [x] 支持最新版的GPT-3.5 chat completion流式输出
- [x] 支持文本向量计算
- [x] 支持获取文件列表
- [x] 支持上传文件
- [x] 支持删除文件
- [x] 支持检索文件
- [x] 支持根据描述生成图片
- [x] 支持根据描述修改图片
- [x] 支持变化图片，类似ai重做图片
- [x] 支持语音转文字
- [x] 支持语音翻译：目前仅支持翻译为英文
- [x] 支持自定义OkhttpClient


## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.openbytecode</groupId>
    <artifactId>open-starter-openai</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 相关配置

```yaml
com:
  openbytecode:
    openai:
      token: sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
## 3. 启用 OpenAI

启用 EnableOpenAI 注解后会注入配置
```java
@EnableOpenAI
public class JobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
    }
}
```

## 版本更新说明

### 1.0.0 版本更新说明

OpenAI官方Api的Java SDK，可以快速接入项目使用。支持OpenAI官方大部分接口