# 分布式调度插件

一个基于时间轮算法的分布式任务调度系统

## 实现原理

**任务池（ScheduleTaskPoolManager）：**

具有调度任务的增、删、改、查功能，主要起一个缓存作用，缓存我们业务上的调度任务，用于和业务端交互

**任务队列（ScheduleTaskQueueManager）：**

基于时间轮算法实现，采用 round 型的时间轮（列表中的任务添加 round 属性），具有调度任务的插入，取出功能，系统启动的时候会将任务池中的调度任务初始化到队列

**时间轮（TaskJobScheduler）：**

时间轮每移动到一个刻度时，从任务队列取出对应的任务列表，遍历任务列表，把round值-1，然后取出所有round=0的任务执行。

## 功能

- [x] 支持 cron 表达式

- [x] 具有任务池化能力，可以缓存业务上的调度任务，同时提供增、删、改、查功能

- [x] 基于时间轮算法实现

- [x] 支持多节点同时调度任务不重复执行，基于分布式锁实现

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-schedule</artifactId>
    <version>1.0.4</version>
</dependency>
```

### 2. 配置参数

```yaml
com:
  saucesubfresh:
    schedule:
      # 配置时间轮槽数
      tick-duration: 60
      # 配置使用 redis 管理调度任务时的 hashKey
      task-pool-name: "schedule:task:pool"
      # 配置使用 redisson 分布式锁时的 lockName
      lock-name: "schedule:lock"
```

### 3. 实现调度任务加载接口

实现 ScheduleTaskLoader 接口，在项目启动时加载调度任务到 ScheduleTaskPoolManager

节选自 Open-Job

```java
@Service
public class OpenJobServiceImpl extends ServiceImpl<OpenJobMapper, OpenJobDO> implements OpenJobService, ScheduleTaskLoader {

    private final OpenJobMapper openJobMapper;
    private final ScheduleTaskExecutor scheduleTaskExecutor;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;

    public OpenJobServiceImpl(OpenJobMapper openJobMapper,
                              ScheduleTaskExecutor scheduleTaskExecutor,
                              ScheduleTaskPoolManager scheduleTaskPoolManager) {
        this.openJobMapper = openJobMapper;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
    }
   
    @Override
    public List<ScheduleTask> loadScheduleTask() {
        List<OpenJobDO> scheduleTasks = openJobMapper.queryStartJob();
        if (CollectionUtils.isEmpty(scheduleTasks)){
            return Collections.emptyList();
        }
        return scheduleTasks.stream().map(this::createScheduleTask).collect(Collectors.toList());
    }

    private ScheduleTask createScheduleTask(OpenJobDO openJobDO){
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setTaskId(openJobDO.getId());
        scheduleTask.setCronExpression(openJobDO.getCronExpression());
        return scheduleTask;
    }
}

```

### 4. 实现业务上的动态增删改查调度任务

主要依赖于 ScheduleTaskPoolManager，通过调用 ScheduleTaskPoolManager 的增删改查能力实现

节选自 Open-Job

```java
@Service
public class OpenJobServiceImpl extends ServiceImpl<OpenJobMapper, OpenJobDO> implements OpenJobService, ScheduleTaskLoader {

    private final OpenJobMapper openJobMapper;
    private final ScheduleTaskExecutor scheduleTaskExecutor;
    private final ScheduleTaskPoolManager scheduleTaskPoolManager;

    public OpenJobServiceImpl(OpenJobMapper openJobMapper,
                              ScheduleTaskExecutor scheduleTaskExecutor,
                              ScheduleTaskPoolManager scheduleTaskPoolManager) {
        this.openJobMapper = openJobMapper;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
        this.scheduleTaskPoolManager = scheduleTaskPoolManager;
    }

    @Override
    public boolean updateById(OpenJobUpdateDTO openJobUpdateDTO) {
        OpenJobDO openJobDO = openJobMapper.selectById(openJobUpdateDTO.getId());
        OpenJobDO convert = OpenJobConvert.INSTANCE.convert(openJobUpdateDTO);
        int update = openJobMapper.updateById(convert);
        updateScheduleTask(openJobDO, convert);
        return update != 0;
    }

    @Override
    public boolean start(Long id) {
        OpenJobDO openJobDO = openJobMapper.selectById(id);
        openJobDO.setStatus(CommonStatusEnum.YES.getValue());
        openJobMapper.updateById(openJobDO);
        ScheduleTask scheduleTask = createScheduleTask(openJobDO);
        try {
            scheduleTaskPoolManager.add(scheduleTask);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean stop(Long id) {
        OpenJobDO openJobDO = openJobMapper.selectById(id);
        openJobDO.setStatus(CommonStatusEnum.NO.getValue());
        openJobMapper.updateById(openJobDO);
        try {
            scheduleTaskPoolManager.remove(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean deleteById(Long id) {
        openJobMapper.deleteById(id);
        try {
            scheduleTaskPoolManager.remove(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void updateScheduleTask(OpenJobDO oldJob, OpenJobDO newJob){
        boolean status = oldJob.getStatus().equals(CommonStatusEnum.YES.getValue());
        boolean equals = StringUtils.equals(oldJob.getCronExpression(), newJob.getCronExpression());
        if (!equals && status){
            ScheduleTask scheduleTask = createScheduleTask(newJob);
            scheduleTaskPoolManager.add(scheduleTask);
        }
    }

    private ScheduleTask createScheduleTask(OpenJobDO openJobDO){
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setTaskId(openJobDO.getId());
        scheduleTask.setCronExpression(openJobDO.getCronExpression());
        return scheduleTask;
    }
}

```

### 5. 高阶用法-集群部署

如果你的调度任务是集群部署模式，请注入以下组件替换系统默认注入的组件
 
1. RedisScheduleTaskPoolManager

使用 redis 管理的任务池

2. DistributedTaskJobScheduler

使用分布式锁控制调度任务的执行

### 6. 关于分布式调度-分布式锁使用的说明

**1. 加锁的粒度要细：**

只在任务执行的时候才进行加锁，也就是只有抢到了锁的机器才能去执行任务，

这样保证每个机器每次都能更新自己机器上任务队列中的任务的下次执行时间，确保机器在抢到锁执行任务的时候不会出现任务遗漏

**2.为什么只能使用 trylock 不能使用 lock？**

因为 lock 在未获取到锁的时候会阻塞当前线程，如果某个机器长时间未获取到锁，那么该机器上的任务队列必定是延后的

而 trylock 是可以设置等待时间的，在等待时间内阻塞当前线程，当超过等待时间后会不在阻塞。


## 1.0.0 版本

1. 任务队列采用 redis zset 实现

## 1.0.2 版本更新说明

1. 对 1.0.0 版本进行重构，摒弃了任务队列采用 redis zset 的实现方式，换为了采用时间轮算法的实现方式

2. 新增了分布式调度能力

## 1.0.3 版本更新说明

修复了当任务列表为空时，导致scheduleThread线程占用大量cpu bug with issues#4 bug

## 1.0.4 版本更新说明

修复了时间轮圈数更新bug，fix bug with issues#20