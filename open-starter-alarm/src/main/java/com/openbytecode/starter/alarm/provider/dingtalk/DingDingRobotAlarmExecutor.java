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
package com.openbytecode.starter.alarm.provider.dingtalk;

import com.openbytecode.starter.alarm.AbstractAlarmExecutor;
import com.openbytecode.starter.alarm.exception.AlarmException;
import com.openbytecode.starter.alarm.properties.AlarmProperties;
import com.openbytecode.starter.alarm.properties.DingDingRobotAlarmProperties;
import com.openbytecode.starter.alarm.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

/**
 * 钉钉机器人报警
 * @author lijunping
 */
@Slf4j
public class DingDingRobotAlarmExecutor extends AbstractAlarmExecutor<DingDingRobotAlarmRequest> {
    private static final String SIGN_METHOD = "HmacSHA256";
    private static final String RESPONSE_MSG = "errmsg";
    private static final String RESPONSE_CODE = "errcode";
    private final DingDingRobotAlarmProperties alarmProperties;

    public DingDingRobotAlarmExecutor(AlarmProperties alarmProperties) {
        this.alarmProperties = alarmProperties.getDingDing();
    }

    @Override
    public void sendAlarm(DingDingRobotAlarmRequest message) throws AlarmException {
        String errMsg;
        String errCode = "";
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            String url = getSignUrl(message);
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

    /**
     * Sign webhook url using secret and timestamp
     */
    private String getSignUrl(DingDingRobotAlarmRequest message) {
        DingDingRobotAlarmRequest.ConfigVO config = message.getConfig();
        String webhook = alarmProperties.getWebhook();
        if (Objects.nonNull(config) && StringUtils.isNotBlank(config.getWebhook())){
            webhook = config.getWebhook();
        }
        String secret = alarmProperties.getSecret();
        if (Objects.nonNull(config) && StringUtils.isNotBlank(config.getSecret())){
            secret = config.getSecret();
        }

        try {
            Long timestamp = System.currentTimeMillis();
            return String.format("%s&timestamp=%s&sign=%s", webhook, timestamp, sign(timestamp, secret));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sign webhook url using HmacSHA256 algorithm
     */
    private String sign(final Long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance(SIGN_METHOD);
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIGN_METHOD));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), StandardCharsets.UTF_8.name());
    }
}
