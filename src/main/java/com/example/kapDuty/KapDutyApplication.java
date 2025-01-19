package com.example.kapDuty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.kapturecrm.*", "com.example.kapDuty.*"})
public class KapDutyApplication {

    public static void main(String[] args) {
        SpringApplication.run(KapDutyApplication.class, args);
    }

}
