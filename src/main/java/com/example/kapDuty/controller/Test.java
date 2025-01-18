package com.example.kapDuty.controller;

import com.example.kapDuty.dto.ErrorLogDto;
import com.example.kapDuty.service.KapLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Test {

    private final KapLogService kapLogService;

    @GetMapping("test")
    public String test() {
        return "Hello world";
    }

    @PostMapping("/store-logs")
    public ResponseEntity<?> storeLogs(@RequestBody ErrorLogDto errorLogDto) {
        return kapLogService.storeErrorLogs(errorLogDto);
    }

}
