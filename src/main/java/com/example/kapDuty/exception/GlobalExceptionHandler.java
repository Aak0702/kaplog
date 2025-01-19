package com.example.kapDuty.exception;

import com.example.kapDuty.service.OpenDutyAlertService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final OpenDutyAlertService openDutyAlertService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();

        String className = stackTrace.length > 0 ? stackTrace[0].getClassName() : "Unknown Class";
        String methodName = stackTrace.length > 0 ? stackTrace[0].getMethodName() : "Unknown Method";

        logger.error("Exception thrown in class: {}, method: {}", className, methodName);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "An unexpected error occurred");
        response.put("details", ex.getMessage());
        response.put("source", "Class: " + className + ", Method: " + methodName); // Include source information
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        // openDutyAlertService.sendAlert(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
