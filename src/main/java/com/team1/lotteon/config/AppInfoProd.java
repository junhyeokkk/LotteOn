package com.team1.lotteon.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Profile("prod")
public class AppInfoProd {

    @Value("${spring.application.version}")
    private String appVersion;


    @Value("${spring.application.name}")
    private String appName;



//    @Value("${spring.application.name}")
//    private String appName;
//
//    @Value("${spring.application.version}")
//    private String appVersion;





}

