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
package com.openbytecode.starter.oauth.domain;

import java.io.Serializable;

/**
 * 用户社交信息
 *
 * @author lijunping
 */
public interface ConnectionDetails extends Serializable {

    /**
     * 服务提供商
     *
     * @return
     */
    String getProviderId();

    /**
     * openId
     *
     * @return
     */
    String getOpenId();

    /**
     * 绑定用户 id
     *
     * @return
     */
    Long getUserId();

    /**
     * 是否绑定
     *
     * @return
     */
    boolean isBind();

}
