package com.saucesubfresh.starter.cache.message;


import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class KafkaCacheMessageListener extends AbstractCacheMessageListener implements MessageListener<String, CacheMessage> {

    private final CacheProperties cacheProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageListener(CacheExecutor cacheExecutor,
                                     CacheProperties cacheProperties,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        super(cacheExecutor);
        this.cacheProperties = cacheProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String namespace = cacheProperties.getNamespace();
        try {
            kafkaTemplate.send(namespace, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }

    @Override
    public void onMessage(ConsumerRecord<String, CacheMessage> consumerRecord) {
        CacheMessage message = consumerRecord.value();
        super.onMessage(message);
    }
}
