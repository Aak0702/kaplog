package com.example.kapDuty.service;

import com.example.kapDuty.constants.GlobalConstant;
import com.example.kapDuty.dto.ErrorLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KapLogService {

    private final KafkaTemplate<String, ErrorLogDto> errorLogKafkaTemplate;

    public ResponseEntity<?> storeErrorLogs(ErrorLogDto errorLogDto) {
        try {
            errorLogKafkaTemplate.send(GlobalConstant.ERROR_LOG_TOPIC, errorLogDto);
            log.info("Message Sent Successfully");
            return new ResponseEntity<>("Details Sent to ErrorLogTopic successFully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred while sending message into ErrorLogTopic");
            return new ResponseEntity<>("Error Occurred while sending message into ErrorLogTopic", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
