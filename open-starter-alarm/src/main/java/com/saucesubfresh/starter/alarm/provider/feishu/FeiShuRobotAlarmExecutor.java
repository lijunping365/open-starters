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
package com.saucesubfresh.starter.alarm.provider.feishu;

import com.saucesubfresh.starter.alarm.AbstractAlarmExecutor;
import com.saucesubfresh.starter.alarm.exception.AlarmException;
import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
import com.saucesubfresh.starter.alarm.properties.FeiShuRobotAlarmProperties;
import com.saucesubfresh.starter.alarm.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Map;
import java.util.Objects;


/**
 * 企业微信自定义机器人报警
 * @author lijunping
 */
@Slf4j
public class FeiShuRobotAlarmExecutor extends AbstractAlarmExecutor<FeiShuRobotAlarmRequest> {

    private static final String RESPONSE_MSG = "StatusMessage";
    private static final String RESPONSE_CODE = "StatusCode";
    private final FeiShuRobotAlarmProperties alarmProperties;

    public FeiShuRobotAlarmExecutor(AlarmProperties alarmProperties){
        this.alarmProperties = alarmProperties.getFeiShu();
    }
    @Override
    public void doAlarm(FeiShuRobotAlarmRequest message) throws AlarmException {
        String errMsg;
        String errCode = "";
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            String url = alarmProperties.getWebhook();
            FeiShuRobotAlarmRequest.ConfigVO config = message.getConfig();
            if (Objects.nonNull(config) && StringUtils.isNotBlank(config.getWebhook())){
                url = config.getWebhook();
            }

            String response = sendAlarmMessage(httpClient, url, JSON.toJSON(message));
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
