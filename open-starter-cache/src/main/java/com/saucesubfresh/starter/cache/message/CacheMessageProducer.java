package com.saucesubfresh.starter.cache.message;

/**
 * <b>
 *     发送消息给其他节点，广播模式
 *
 *     目的就是使其他节点的本地缓存同步
 *
 *     具体实现可参考 {@link RedissonCacheMessageProducer,
 *                  @link RedisCacheMessageProducer,
 *                  @link KafkaCacheMessageProducer}
 * </b>
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public interface CacheMessageProducer {

    /**
     * 通知其他节点同步缓存
     *
     * @param message 消息
     */
    void broadcastLocalCacheStore(CacheMessage message);
}
