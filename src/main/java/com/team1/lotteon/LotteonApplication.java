package com.team1.lotteon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LotteonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteonApplication.class, args);
    }

}
