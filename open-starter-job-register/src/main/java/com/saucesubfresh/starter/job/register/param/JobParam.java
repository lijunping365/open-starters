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
package com.saucesubfresh.starter.job.register.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author lijunping
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class JobParam implements Serializable {
    private static final long serialVersionUID = 1135284528376646225L;
    /**
     * The Id of job
     */
    private Long jobId;
    /**
     * The params of job
     */
    private String params;
    /**
     * The script of job
     */
    private String script;
    /**
     * The update time of script
     */
    private String scriptUpdateTime;
    /**
     * 任务分片节点列表
     */
    private List<String> shardingNodes;
}
