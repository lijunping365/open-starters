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
package com.openbytecode.starter.oauth.service;

import com.openbytecode.starter.oauth.domain.ConnectionDetails;

/**
 * 用户社交信息服务
 *
 * @author lijunping
 */
public interface UserConnectionService {

    /**
     * 根据 providerId 和 openId 在connection 表中查找是否存在一条 connection 数据
     *
     * @param providerId
     * @param openId
     * @return
     */
    ConnectionDetails loadConnectionByProviderIdAndOpenId(String providerId, String openId);

    /**
     * 保存 userId 和社交信息到 在connection 表
     *
     * @param userId
     * @param providerId
     * @param openId
     */
    void saveConnectionDetails(Long userId, String providerId, String openId);
}
