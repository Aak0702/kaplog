package com.example.kapDuty.kafka;

import com.example.kapDuty.dto.ErrorLogDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ErrorLogDeserializer implements Deserializer<ErrorLogDto> {

    @Override
    public ErrorLogDto deserialize(String topic, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ErrorLogDto errorLogDto = null;
        try {
            errorLogDto = mapper.readValue(bytes, ErrorLogDto.class);
        } catch (Exception e) {
            log.error("Error in GreenEnergyEnrolledDeserializer : ", e);
        }
        return errorLogDto;
    }

}
