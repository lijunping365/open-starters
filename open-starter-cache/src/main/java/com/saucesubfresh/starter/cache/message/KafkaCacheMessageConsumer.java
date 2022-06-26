package com.saucesubfresh.starter.cache.message;

import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:26
 */
public class KafkaCacheMessageConsumer extends AbstractCacheMessageConsumer implements MessageListener<String, CacheMessage> {

    public KafkaCacheMessageConsumer(CacheExecutor cacheExecutor) {
        super(cacheExecutor);
    }

    @Override
    public void onMessage(ConsumerRecord<String, CacheMessage> consumerRecord) {
        CacheMessage message = consumerRecord.value();
        super.onMessage(message);
    }
}
