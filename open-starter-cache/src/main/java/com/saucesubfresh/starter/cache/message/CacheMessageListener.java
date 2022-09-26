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

/**
 * <b>
 *     监听广播消息
 *
 *     目的就是使其他节点的本地缓存同步
 *
 *     具体实现可参考 {@link RedissonCacheMessageListener ,
 *                  @link RedisCacheMessageListener ,
 *                  @link KafkaCacheMessageListener}
 * </b>
 * @author: 李俊平
 * @Date: 2022-06-25 17:14
 */
public interface CacheMessageListener {

    /**
     * 接受同步消息，同步本地缓存
     *
     * @param message 消息
     */
    void onMessage(CacheMessage message);
}
