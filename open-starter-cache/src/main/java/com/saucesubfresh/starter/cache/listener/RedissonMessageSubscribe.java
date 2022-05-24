package com.saucesubfresh.starter.cache.listener;

import org.redisson.api.listener.MessageListener;

/**
 * @author lijunping on 2022/5/24
 */
public class RedissonMessageSubscribe implements MessageListener<Message>, MessageSubscribe{

    @Override
    public void onMessage(String channel, Message message) {

    }

    @Override
    public void onMessage(CharSequence channel, Message message) {

    }
}
