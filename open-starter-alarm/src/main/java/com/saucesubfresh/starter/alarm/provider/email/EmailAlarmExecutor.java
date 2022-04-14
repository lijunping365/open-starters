package com.saucesubfresh.starter.alarm.provider.email;

import com.saucesubfresh.starter.alarm.AlarmExecutor;
import com.saucesubfresh.starter.alarm.callback.AlarmCallback;
import com.saucesubfresh.starter.alarm.callback.AlarmCallbackMessage;
import com.saucesubfresh.starter.alarm.properties.AlarmProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author lijunping on 2022/4/14
 */
@Slf4j
public class EmailAlarmExecutor implements AlarmExecutor<EmailMessageRequest> {
    private final JavaMailSender mailSender;
    private final AlarmProperties alarmProperties;

    public EmailAlarmExecutor(JavaMailSender mailSender, AlarmProperties alarmProperties) {
        this.mailSender = mailSender;
        this.alarmProperties = alarmProperties;
    }

    @Override
    public void doAlarm(EmailMessageRequest message, AlarmCallback callback) {
        final String alarmEmail = message.getAlarmEmail();
        if (StringUtils.isBlank(alarmEmail)){
            return;
        }

        Set<String> emailSet = new HashSet<>(Arrays.asList(alarmEmail.split(",")));
        Map<String, String> map = new HashMap<>();
        emailSet.forEach(email->{
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(alarmProperties.getEmailFrom());
                helper.setTo(email);
                helper.setSubject(message.getTitle());
                helper.setText(message.getContent(), true);
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                log.error("alarm email send error: {} - {}", e.getMessage(), e);
                map.put(email, e.getMessage());
            }
        });

        AlarmCallbackMessage callbackMessage = new AlarmCallbackMessage();
        callbackMessage.setAlarmMessage(message);
        callbackMessage.setFailedSender(map);
        callbackMessage.setTime(LocalDateTime.now());
        callback.callback(callbackMessage);
    }
}
