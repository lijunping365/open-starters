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


import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author lijunping
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
        String instanceId = properties.getInstanceId();
        message.setInstanceId(instanceId);
        try {
            kafkaTemplate.send(namespace, message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
