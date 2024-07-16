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
package com.openbytecode.starter.alarm.provider.feishu;

import com.openbytecode.starter.alarm.AbstractAlarmExecutor;
import com.openbytecode.starter.alarm.exception.AlarmException;
import com.openbytecode.starter.alarm.properties.AlarmProperties;
import com.openbytecode.starter.alarm.properties.FeiShuRobotAlarmProperties;
import com.openbytecode.starter.alarm.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Map;


/**
 * 飞书自定义机器人报警
 * @author lijunping
 */
@Slf4j
public class FeiShuRobotAlarmExecutor extends AbstractAlarmExecutor<FeiShuRobotAlarmRequest> {

    private static final String RESPONSE_MSG = "msg";
    private static final String RESPONSE_CODE = "code";
    private final FeiShuRobotAlarmProperties alarmProperties;

    public FeiShuRobotAlarmExecutor(AlarmProperties alarmProperties){
        this.alarmProperties = alarmProperties.getFeiShu();
    }
    @Override
    public void sendAlarm(FeiShuRobotAlarmRequest message) throws AlarmException {
        String errMsg;
        String errCode = "";
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            String webHook = super.getWebHook(alarmProperties.getWebhook(), message);
            String response = sendAlarmMessage(httpClient, webHook, JSON.toJSON(message));
            Map<String, String> res = JSON.parseMap(response, String.class, String.class);
            errMsg = res.get(RESPONSE_MSG);
            errCode = res.get(RESPONSE_CODE);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }

        if (!StringUtils.equals(errCode, "0")){
            throw new AlarmException(errMsg);
        }
    }
}
