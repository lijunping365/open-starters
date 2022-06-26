package com.saucesubfresh.starter.cache.message;

/**
 * <b>
 *     接受其他节点发送的广播消息，同步本地缓存
 *
 *     具体实现可参考 {@link RedissonCacheMessageConsumer,
 *                  @link RedisCacheMessageConsumer,
 *                  @link KafkaCacheMessageConsumer}
 *
 * </b>
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public interface CacheMessageConsumer {

    /**
     * 接受同步消息，同步本地缓存
     *
     * @param message 消息
     */
    void onMessage(CacheMessage message);
}
