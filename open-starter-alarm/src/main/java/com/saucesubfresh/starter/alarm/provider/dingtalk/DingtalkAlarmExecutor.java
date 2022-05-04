package com.saucesubfresh.starter.alarm.provider.dingtalk;

import com.saucesubfresh.starter.alarm.AbstractAlarmExecutor;
import com.saucesubfresh.starter.alarm.exception.AlarmException;
import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
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
 * @author lijunping on 2022/4/14
 */
@Slf4j
public class DingtalkAlarmExecutor extends AbstractAlarmExecutor<DingtalkMessageRequest> {
    private static final String SIGN_METHOD = "HmacSHA256";
    private static final String RESPONSE_MSG = "errmsg";
    private static final String SUCCESS_SIGN = "ok";
    private final AlarmProperties alarmProperties;

    public DingtalkAlarmExecutor(AlarmProperties alarmProperties) {
        this.alarmProperties = alarmProperties;
    }

    @Override
    public void doAlarm(DingtalkMessageRequest message) throws AlarmException {
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
