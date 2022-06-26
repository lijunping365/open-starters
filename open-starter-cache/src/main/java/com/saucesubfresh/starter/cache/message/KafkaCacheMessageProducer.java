package com.saucesubfresh.starter.cache.message;


import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public class KafkaCacheMessageProducer implements CacheMessageProducer{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        String topic = message.getTopic();
        kafkaTemplate.send(topic, message);
    }
}
