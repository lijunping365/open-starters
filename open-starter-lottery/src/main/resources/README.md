# 抽奖系统

一个功能强能强大抽奖系统，抽奖概率样本既可以是奖品也可以是抽奖者

对外提供接口提供创建活动接口

参与次数还有参与等级限制等规则

拦截器的参数如何设计库表

1. 活动关联拦截器

2. 中奖者名单

开奖方式： 即时抽奖

其中：定时开奖，手动开奖，满人开奖 都可以通过消息系统把中奖结果可以异步通知给中奖者

然后 rpc 消费者扫描中奖者名单表进行发奖



# 抽奖系统

## 目前常见的抽奖玩法

### 1. 用户抽中奖中奖率只跟奖品有关

特点：

> 这种抽奖玩法对抽奖者来说是相对公平的
> 这种抽奖往往在用户抽完奖就可以知道抽奖结果

随机一个奖品给用户

### 2. 中奖率和抽奖人有关

特点：

> 这种抽奖玩法对抽奖者来说是不公平的，但是这种玩法也是目前使用最多的
> 通常这种抽奖都有参与时间限制，比如 10 分钟后自动出抽奖结果
> 用户参与抽奖的方式比较特殊，通常是做任务或者充值

例一：各大直播间刷礼物抽奖，这种抽奖模式其实是一种伪抽奖，因为用户的中奖率是由用户刷的礼物多少来决定的，也就是说你刷的礼物越多，中奖概率就越大，典型的人名币游戏

目的：为了割韭菜

例二：各大直播间的弹幕抽奖，这种抽奖模式其实也是一种伪抽奖，因为用户的中奖率是由用户发的弹幕多少来决定的（而且与粉丝的等级等也有关系）

目的：为了增加直播间的活跃度





















## 设计的模块

1. 奖品
2. 概率





## 考虑这些功能放在哪里合适

功能：

1. 扣减库存，
2. 插入中奖名单表，
3. 通知中奖者

选项：

1. 抽奖后置拦截器
2. 成功处理器




重要！！！

抽奖都会返回结果，中奖通知方式由调用方自行实现。



# 架构解说

主要思想：就是先按照一定规则把奖品放到不同的柜子中并生成

1. 两条路线

（1）初始化路线

- 

（2）抽奖路线



# 1.手气红包



# 2.转盘抽奖


# 3.一毛钱定时抽奖






















