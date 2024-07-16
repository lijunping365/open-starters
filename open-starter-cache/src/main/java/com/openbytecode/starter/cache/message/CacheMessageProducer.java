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

/**
 * <b>
 *     发送消息给其他节点，广播模式
 *
 *     目的就是使其他节点的本地缓存同步
 *
 *     具体实现可参考 {@link RedissonCacheMessageProducer},{@link RedisCacheMessageProducer},{@link KafkaCacheMessageProducer}
 * </b>
 *
 * @author lijunping
 */
public interface CacheMessageProducer {

    /**
     * 通知其他节点同步缓存
     *
     * @param message 消息
     */
    void broadcastLocalCacheStore(CacheMessage message);
}
