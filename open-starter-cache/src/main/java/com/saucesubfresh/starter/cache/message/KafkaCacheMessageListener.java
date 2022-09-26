/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageListener(CacheExecutor cacheExecutor,
                                     CacheProperties cacheProperties,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        super(cacheExecutor, cacheProperties);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onMessage(ConsumerRecord<String, CacheMessage> consumerRecord) {
        CacheMessage message = consumerRecord.value();
        log.info("received a message, cacheName={}", message.getCacheName());
        super.onMessage(message);
    }
}
