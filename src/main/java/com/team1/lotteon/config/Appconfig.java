package com.team1.lotteon.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Appconfig {
    @Bean
    public AppInfo getAppInfo() {
        return new AppInfo();
    }

    @Bean
    public ModelMapper getModelMapper(){
        // DTO와 Entity간 변환을 위한 ModelMapper 설정
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}
