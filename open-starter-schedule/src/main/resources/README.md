# 分布式调度插件

一个基于时间轮算法的任务调度组件

## 实现原理

**时间轮（TimeWheel）：**

基于时间轮算法实现，采用 round 型的时间轮（列表中的任务添加 round 属性），具有调度任务的插入，取出功能，系统启动的时候会将任务池中的调度任务初始化到队列

**定时器（TaskJobScheduler）：**

定时器每秒移动一个刻度，从时间轮取出对应的任务列表，遍历任务列表，把round值-1，然后取出所有round=0的任务执行。

## 功能

- [x] 支持 cron 表达式

- [x] 基于时间轮算法实现

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-schedule</artifactId>
    <version>1.1.0</version>
</dependency>
```

### 2. 配置参数

```yaml
com:
  saucesubfresh:
    schedule:
      # 配置时间轮槽数
      tick-duration: 60
```

### 3. 实现调度任务加载接口

实现 ScheduleTaskService 接口

## 1.0.0 版本

1. 任务队列采用 redis zset 实现

## 1.0.2 版本更新说明

1. 对 1.0.0 版本进行重构，摒弃了任务队列采用 redis zset 的实现方式，换为了采用时间轮算法的实现方式

2. 新增了分布式调度能力

## 1.0.3 版本更新说明

修复了当任务列表为空时，导致scheduleThread线程占用大量cpu bug with issues#4 bug

## 1.0.4 版本更新说明

修复了时间轮圈数更新bug，fix bug with issues#20

## 1.1.0 版本更新说明

组件重构，去掉了任务池，新增了触发器