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
package com.saucesubfresh.starter.alarm.provider.dingtalk;

import com.saucesubfresh.starter.alarm.AbstractAlarmExecutor;
import com.saucesubfresh.starter.alarm.exception.AlarmException;
import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
import com.saucesubfresh.starter.alarm.properties.DingDingAlarmProperties;
import com.saucesubfresh.starter.alarm.utils.JSON;
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

/**
 * 钉钉机器人报警
 * @author lijunping
 */
@Slf4j
public class DingDingAlarmExecutor extends AbstractAlarmExecutor<DingDingMessageRequest> {
    private static final String SIGN_METHOD = "HmacSHA256";
    private static final String RESPONSE_MSG = "errmsg";
    private static final String SUCCESS_SIGN = "ok";
    private final DingDingAlarmProperties alarmProperties;

    public DingDingAlarmExecutor(AlarmProperties alarmProperties) {
        this.alarmProperties = alarmProperties.getDingDing();
    }

    @Override
    public void doAlarm(DingDingMessageRequest message) throws AlarmException {
        String errMsg;
        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            String url = getSignUrl();
            String response = sendAlarmMessage(httpClient, url, JSON.toJSON(message));
            errMsg = JSON.parseMap(response, String.class, String.class).get(RESPONSE_MSG);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }

        if (!StringUtils.equals(errMsg, SUCCESS_SIGN)){
            throw new AlarmException(errMsg);
        }
    }

    /**
     * Sign webhook url using secret and timestamp
     */
    private String getSignUrl() {
        try {
            Long timestamp = System.currentTimeMillis();
            return String.format("%s&timestamp=%s&sign=%s", alarmProperties.getWebhook(), timestamp, sign(timestamp, alarmProperties.getSecret()));
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
