/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.cache.message;


import com.openbytecode.starter.cache.exception.CacheBroadcastException;
import com.openbytecode.starter.cache.properties.CacheProperties;
import com.openbytecode.starter.cache.handler.CacheProducerErrorHandler;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author lijunping
 */
public class KafkaCacheMessageProducer extends AbstractCacheMessageProducer{

    private final CacheProperties properties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaCacheMessageProducer(CacheProperties properties,
                                     CacheProducerErrorHandler errorHandler,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
        super(properties, errorHandler);
        this.properties = properties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void broadcastCacheMessage(CacheMessage message) {
        String namespace = properties.getNamespace();
        try {
            kafkaTemplate.send(namespace, message);
        }catch (Exception e){
            throw new CacheBroadcastException(e.getMessage(), e);
        }
    }
}
