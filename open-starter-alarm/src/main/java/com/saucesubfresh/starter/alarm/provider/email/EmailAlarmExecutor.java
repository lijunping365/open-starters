package com.saucesubfresh.starter.alarm.provider.email;

import com.saucesubfresh.starter.alarm.AlarmExecutor;
import com.saucesubfresh.starter.alarm.exception.AlarmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lijunping on 2022/4/14
 */
@Slf4j
public class EmailAlarmExecutor implements AlarmExecutor<EmailMessageRequest> {
    private final JavaMailSender mailSender;

    public EmailAlarmExecutor(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void doAlarm(EmailMessageRequest request) throws AlarmException {
        final String alarmEmail = request.getToEmail();
        Set<String> emailSet = new HashSet<>(Arrays.asList(alarmEmail.split(",")));
        emailSet.forEach(email->{
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(request.getFromEmail());
                helper.setTo(email);
                helper.setSubject(request.getTitle());
                helper.setText(request.getContent(), true);
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                log.error("alarm email send error: {} - {}", e.getMessage(), e);
                throw new AlarmException(String.format("send to: %s , occurs exception: %s", email, e.getMessage()));
            }
        });
    }
}
