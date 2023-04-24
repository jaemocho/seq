package com.common.seq.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
        info = @Info(title = "채번 & shop API 명세서",
                description = "채번 & shop API 서비스 명세서",

                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
 
    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/seq/**", "/api/v1/shorten-url/**", "/api/v1/auth/**","/api/v1/shop/**"};
 
        return GroupedOpenApi.builder()
                .group("API v1")
                .pathsToMatch(paths)
                .build();
    }
}