package com.common.seq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SequrityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
			.authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated().and()
            .formLogin().and()
            .csrf().disable()
            .headers()
            .frameOptions().sameOrigin()
            ;
		
        return http.build();
    }
}
