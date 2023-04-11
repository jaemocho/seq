package com.common.seq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.common.seq.common.auth.JWTAuthenticationEntryPoint;
import com.common.seq.common.auth.JWTAuthenticationFilter;
import com.common.seq.common.auth.JWTProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SequrityConfig {

    // private final AuthFailureHandler authFailureHandler;
    
    // private final AuthSuccessHandler authSuccessHandler;

    private final JWTProvider jwtProvider;

    private final CorsFilter corsFilter;

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            
            // jwt 사용으로 form login 관련 부분 disable 및 customhandler 주석 처리
            .formLogin().disable()
            .httpBasic().disable()
            // .successHandler(authSuccessHandler)
            // .failureHandler(authFailureHandler)

            // enable h2-console
            .headers()
            .frameOptions()
            .sameOrigin()

            // session 사용 안함
            .and()
            .sessionManagement() 
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // api 경로 
            .and()
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers(toH2Console()).permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())
            
            .addFilter(corsFilter)
            .addFilterBefore(new JWTAuthenticationFilter(jwtProvider)
                        , UsernamePasswordAuthenticationFilter.class)
            
            // 상세한 인증/인가 관련 핸들링이 필요할 때 작성
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 실패 시 핸들링 implements AuthenticationEntryPoint 
            // .accessDeniedHandler(jwtAccessDeniedHandler) // 인가 실패 시 핸들링 implements AccessDeniedHandler 
            ;
		
        return http.build();
    }

}
