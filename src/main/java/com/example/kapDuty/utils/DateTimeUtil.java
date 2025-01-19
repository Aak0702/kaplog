package com.example.kapDuty.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateTimeUtil {

    public static Date getCurrDateTime() {
        return Calendar.getInstance().getTime();
    }

    public static Timestamp getCurrTimestamp() {
        return Timestamp.from(Instant.now());
    }

}
