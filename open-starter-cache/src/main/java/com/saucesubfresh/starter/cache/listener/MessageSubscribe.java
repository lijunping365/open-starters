package com.saucesubfresh.starter.cache.listener;

/**
 * 订阅消息
 * @author lijunping on 2022/5/24
 */
public interface MessageSubscribe {

    void onMessage(String channel, Message message);
}
