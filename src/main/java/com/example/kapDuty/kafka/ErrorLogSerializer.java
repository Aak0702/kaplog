package com.example.kapDuty.kafka;

import com.example.kapDuty.dto.ErrorLogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class ErrorLogSerializer implements Serializer<ErrorLogDto> {

    @Override
    public byte[] serialize(String topic, ErrorLogDto errorLogDto) {
        byte[] data = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            data = objectMapper.writeValueAsString(errorLogDto).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
