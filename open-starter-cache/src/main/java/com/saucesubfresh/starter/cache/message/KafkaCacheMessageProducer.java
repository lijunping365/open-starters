package com.saucesubfresh.starter.cache.message;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
@Slf4j
public class KafkaCacheMessageProducer implements CacheMessageProducer{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topic = message.getTopic();
        try {
            kafkaTemplate.send(topic, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
