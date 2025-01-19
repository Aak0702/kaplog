package com.example.kapDuty.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogMonitor {

    private static final String LOG_FILE_PATH = "logs/kap-duty.log";
    private static final String ERROR_KEYWORD = "ERROR GlobalExceptionHandler";

    public void checkLogsForErrors() {
        File logFile = new File(LOG_FILE_PATH);
        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(ERROR_KEYWORD)) {
                    sendAlert(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAlert(String logMessage) {
        System.out.println("Sending alert: " + logMessage);
        MailUtil.sendMail("Alert exception", logMessage);
    }
}
