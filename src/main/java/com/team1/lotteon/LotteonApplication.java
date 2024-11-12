package com.team1.lotteon;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class LotteonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteonApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Set the default timezone to Asia/Seoul
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
