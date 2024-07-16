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
package com.openbytecode.starter.executor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 任务执行配置类
 *
 * @author lijunping
 */
@Data
@ConfigurationProperties("com.openbytecode.executor")
public class TaskExecutorProperties {

  /**
   * 超时时间，默认 3，单位 second
   */
  private Integer timeout = 3;

  /**
   * 最大心跳次数，默认 30
   */
  private Integer maxIdleTimes = 30;

  /**
   * 核心线程数
   */
  private Integer corePoolSize = 5;

  /**
   * 最大线程数
   */
  private Integer maximumPoolSize = 10;

  /**
   * 线程空闲时间
   */
  private Long keepAliveTime = 60L;

  /**
   * 队列大小
   */
  private Integer queueCapacity = 100;

  /**
   * 任务执行线程前缀
   */
  private String prefix = "executor-task";

}
