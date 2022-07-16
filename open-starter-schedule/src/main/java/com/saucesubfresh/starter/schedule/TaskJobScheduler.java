package com.saucesubfresh.starter.schedule;

/**
 * 定时器
 *
 * @author: 李俊平
 * @Date: 2022-07-16 11:49
 */
public interface TaskJobScheduler {

    /**
     * 启动定时器
     */
    void start();

    /**
     * 停止定时器
     */
    void stop();
}
