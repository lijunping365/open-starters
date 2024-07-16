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
package com.openbytecode.starter.alarm.provider.email;

import com.openbytecode.starter.alarm.request.BaseAlarmRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lijunping
 */
@Data
public class EmailRequestRequest extends BaseAlarmRequest {

    /**
     * 发件人
     */
    @NotBlank(message = "发件人不能为空")
    private String fromEmail;

    /**
     * 收件人，逗号隔开字符串
     */
    @NotBlank(message = "收件人不能为空")
    private String toEmail;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 报警内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

}
