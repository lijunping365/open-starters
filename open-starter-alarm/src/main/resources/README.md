# 报警插件

1. 钉钉自定义机器人报警
2. 邮件报警
3. 企业微信自定义机器人报警
4. 飞书自定义机器人报警
5. 支持配置多个机器人报警

# 注意

邮箱的话需要配置

```properties
### email
spring.mail.host=smtp.qq.com
spring.mail.port=25
spring.mail.username=2544054976@qq.com
spring.mail.password=xxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
```

dingtalk

@指定的人
```java
public class DingtalkTest {

    public static void main(String[] args) {
        // 接收者相关
        DingtalkMessageRequest.AtVO atVo = DingtalkMessageRequest.AtVO.builder().build();
        atVo.setAtMobiles(Collections.singletonList("182****6871"));

        // 消息内容
        DingtalkMessageRequest.TextVO textVO = DingtalkMessageRequest.TextVO.builder().content(contentModel.getContent()).build();

        DingtalkMessageRequest.builder().at(atVo).msgtype("text").text(textVO).build();
    }
}
```

@所有人
```java
public class DingtalkTest {

    public static void main(String[] args) {
        // 接收者相关
        DingtalkMessageRequest.AtVO atVo = DingtalkMessageRequest.AtVO.builder().build();
        atVo.setIsAtAll(true);

        // 消息内容
        DingtalkMessageRequest.TextVO textVO = DingtalkMessageRequest.TextVO.builder().content(contentModel.getContent()).build();

        DingtalkMessageRequest.builder().at(atVo).msgtype("text").text(textVO).build();
    }
}
```

## 版本更新说明

### 1.0.2

1. 集成了钉钉机器人报警

2. 集成了邮件报警

### 1.0.3

1. 集成了企业微信自定义机器人报警

2. 集成了飞书自定义机器人报警

3. 优化报警配置类

4. 支持配置多个机器人报警

## 说明

### 对于多机器人报警使用说明

你需要在 Request 的 Config 中配置你的报警配置，报警获取配置的原理是如果你在 Request 的 Config 配置了

报警配置，那么就会使用你 Request 中 Config 的配置，如果没有配置就会使用全局的 properties 配置。