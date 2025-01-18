package com.example.kapDuty.kafka;

import com.example.kapDuty.constants.GlobalConstant;
import com.example.kapDuty.dto.ErrorLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaConsumer {

    @KafkaListener(topics = GlobalConstant.ERROR_LOG_TOPIC, containerGroup = "error-log-group", containerFactory = "errorLogKafkaListenerContainerFactory")
    public void listen(@Payload ErrorLogDto errorLogDto) {
        try {
            log.info("Consuming ErrorLog: {}", errorLogDto);
        } catch (Exception e) {
            log.error("Error while consuming ErrorLogTopic:" + e);
        }
    }
}
