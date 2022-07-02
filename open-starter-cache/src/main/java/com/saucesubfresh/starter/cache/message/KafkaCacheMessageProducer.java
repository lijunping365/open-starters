package com.saucesubfresh.starter.cache.message;


import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class KafkaCacheMessageProducer implements CacheMessageProducer {

    private final CacheProperties properties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageProducer(CacheProperties properties,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        this.properties = properties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String namespace = properties.getNamespace();
        Long instanceId = properties.getInstanceId();
        message.setInstanceId(instanceId);
        try {
            kafkaTemplate.send(namespace, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
