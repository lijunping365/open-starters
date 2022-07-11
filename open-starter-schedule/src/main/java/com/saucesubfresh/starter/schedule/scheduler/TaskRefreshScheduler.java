package com.saucesubfresh.starter.schedule.scheduler;

/**
 * 定时刷新下次执行时间线程
 * @author: 李俊平
 * @Date: 2022-07-10 15:21
 */
public interface TaskRefreshScheduler {

    void refresh();
}
