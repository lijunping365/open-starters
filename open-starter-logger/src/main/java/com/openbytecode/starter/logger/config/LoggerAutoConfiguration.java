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
package com.openbytecode.starter.logger.config;

import com.openbytecode.starter.logger.DefaultLifeCycle;
import com.openbytecode.starter.logger.DefaultLoggerEvict;
import com.openbytecode.starter.logger.properties.LoggerProperties;
import com.openbytecode.starter.logger.LifeCycle;
import com.openbytecode.starter.logger.LoggerEvict;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * logger autoConfiguration
 *
 * @author lijunping
 */
@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LoggerEvict loggerEvict(LoggerProperties properties){
        return new DefaultLoggerEvict(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public LifeCycle lifeCycle(LoggerProperties properties, LoggerEvict loggerEvict){
        return new DefaultLifeCycle(properties, loggerEvict);
    }

}
