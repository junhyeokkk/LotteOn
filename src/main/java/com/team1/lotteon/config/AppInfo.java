package com.team1.lotteon.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppInfo {

    @Value("${app.version}")
    private String appVersion;


    @Value("${spring.application.name}")
    private String appName;


    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // YAML에서 설정한 파일 업로드 경로
}

