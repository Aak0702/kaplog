package com.example.kapDuty.dto;

import lombok.Data;

@Data
public class ErrorLogDto {
    private String fileName;
    private String errorMessage;
    private String serviceName;
    private String exceptionName;
}
