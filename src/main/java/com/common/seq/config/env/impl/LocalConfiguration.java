package com.common.seq.config.env.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.common.seq.config.env.EnvConfiguration;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("local")
@Configuration
public class LocalConfiguration implements EnvConfiguration {
    
    @Value("${common.loading.message}")
    private String message;

    @Override
    @Bean
    public String getMessage() {
        log.info("[LocalConfiguration]");
        return message;
    }
    
}
