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
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author lijunping
 */
@Slf4j
public class RedissonCacheMessageProducer implements CacheMessageProducer{

    private final RTopic topic;
    private final CacheProperties properties;

    public RedissonCacheMessageProducer(RedissonClient redissonClient, CacheProperties properties) {
        String namespace = properties.getNamespace();
        this.properties = properties;
        this.topic = redissonClient.getTopic(namespace);
    }

    @Override
    public void broadcastLocalCacheStore(CacheMessage message) {
        Long instanceId = properties.getInstanceId();
        message.setInstanceId(instanceId);
        try {
            topic.publish(message);
        }catch (Exception e){
            log.error("发送缓存同步消息失败，{}，{}", e.getMessage(), e);
        }
    }
}
