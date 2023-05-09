# 报警插件

1. 钉钉机器人报警
2. 邮件报警
3. 企业微信报警

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

1. 集成了企业机器人报警

2. 优化报警配置类