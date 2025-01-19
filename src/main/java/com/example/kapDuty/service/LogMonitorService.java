package com.example.kapDuty.service;

import com.example.kapDuty.utils.LogMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogMonitorService {

    private final LogMonitor logMonitor;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void monitorLogs() {
        logMonitor.checkLogsForErrors();
    }
}
