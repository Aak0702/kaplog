package com.example.kapDuty.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenDutyAlertService {

    @Value("${openduty.webhook.url}")
    private String openDutyWebhookUrl;

    private final RestTemplate restTemplate;

    public OpenDutyAlertService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendAlert(String errorMessage) {
        try {
            // Create a JSON payload
            String payload = "{"
                    + "\"service\": \"Spring Boot Application\","
                    + "\"severity\": \"critical\","
                    + "\"message\": \"" + errorMessage + "\","
                    + "\"timestamp\": \"" + System.currentTimeMillis() + "\""
                    + "}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);

            // Send the alert to OpenDuty via HTTP POST
            restTemplate.exchange(openDutyWebhookUrl, HttpMethod.POST, entity, String.class);
        } catch (HttpStatusCodeException e) {
            // Log failure in sending alert
            e.printStackTrace();
        }
    }
}
