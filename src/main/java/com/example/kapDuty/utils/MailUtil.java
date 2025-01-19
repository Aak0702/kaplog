package com.example.kapDuty.utils;

import com.kapturecrm.communications.EmailCommunications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailUtil {

    public static final String EMAIL_TO_SEND = "aakankshabhandari07@gmail.com";
    public static final String EMAIL_CC_TO_SEND = String.join(",", "");
    private static final String EMAIL_FROM = "aakankshabhandari07@gmail.com";

    private static EmailCommunications emailCommunications;

    @Autowired
    public MailUtil(EmailCommunications emailCommunications) {
        MailUtil.emailCommunications = emailCommunications;
    }

    public static void sendMail(String subject, String content) {
        subject = subject.concat(" | " + DateTimeUtil.getCurrTimestamp());
        String timestampAndDate = "<br><br>Timestamp : " + DateTimeUtil.getCurrTimestamp() +
                "<br>Calendar : " + DateTimeUtil.getCurrDateTime() + "<br>";

        content = content.concat(timestampAndDate);

        emailCommunications.sendEmail(EMAIL_TO_SEND, content, subject, EMAIL_CC_TO_SEND, "", EMAIL_FROM, null, null);
    }
}
