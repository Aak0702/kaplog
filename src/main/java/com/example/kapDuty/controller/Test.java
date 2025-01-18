package com.example.kapDuty.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/validate-age")
    public ResponseEntity<String> validateAge(@RequestParam Integer age) {
        if (age == null || age < 18) {
            Integer.parseInt(null);
        }
        return ResponseEntity.ok("Age is valid");
    }

    @GetMapping("/exception")
    public void generalException() {
        throw new RuntimeException("This is a generic exception");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestParam String username, @RequestParam String password) {
        String validUsername = "admin";
        String validPassword = "password";

        if (!username.equals(validUsername) || !password.equals(validPassword)) {
            throw new SecurityException("Invalid username or password");
        }
        return ResponseEntity.ok("Authentication successful");
    }


    @PostMapping("/store-logs")
    public ResponseEntity<?> storeLogs(@RequestBody ErrorLogDto errorLogDto) {
        return kapLogService.storeErrorLogs(errorLogDto);
    }

}
