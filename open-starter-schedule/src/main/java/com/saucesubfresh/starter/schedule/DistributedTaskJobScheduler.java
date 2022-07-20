package com.saucesubfresh.starter.schedule;

import com.saucesubfresh.starter.schedule.executor.ScheduleTaskExecutor;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskPoolManager;
import com.saucesubfresh.starter.schedule.manager.ScheduleTaskQueueManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-07-16 11:49
 */
@Slf4j
public class DistributedTaskJobScheduler extends AbstractTaskJobScheduler {

    private final String lockName = "distributed-scheduler";
    private final RedissonClient redissonClient;
    private final ScheduleTaskExecutor scheduleTaskExecutor;

    public DistributedTaskJobScheduler(RedissonClient redissonClient,
                                       ScheduleTaskExecutor scheduleTaskExecutor,
                                       ScheduleTaskPoolManager scheduleTaskPoolManager,
                                       ScheduleTaskQueueManager scheduleTaskQueueManager) {
        super(scheduleTaskPoolManager, scheduleTaskQueueManager);
        this.redissonClient = redissonClient;
        this.scheduleTaskExecutor = scheduleTaskExecutor;
    }

    @Override
    protected void runTask(List<Long> taskIds) {
        RLock lock = redissonClient.getLock(lockName);
        long waitTime = 1000 - System.currentTimeMillis() % 1000;
        try {
            if (lock.tryLock(waitTime, -1, TimeUnit.MILLISECONDS)){
                scheduleTaskExecutor.execute(taskIds);
                threadSleep();
            }else {
                log.info("排队中");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info("释放了锁");
                lock.unlock();
            }
        }
    }


}
