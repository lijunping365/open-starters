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
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author lijunping
 */
@Slf4j
public class RedissonCacheMessageListener extends AbstractCacheMessageListener {

    public RedissonCacheMessageListener(CacheExecutor cacheExecutor,
                                        RedissonClient redissonClient,
                                        CacheProperties cacheProperties) {
        super(cacheExecutor, cacheProperties);
        String namespace = cacheProperties.getNamespace();
        RTopic topic = redissonClient.getTopic(namespace);
        topic.addListener(CacheMessage.class, (channel, msg) -> super.onMessage(msg));
    }

}
