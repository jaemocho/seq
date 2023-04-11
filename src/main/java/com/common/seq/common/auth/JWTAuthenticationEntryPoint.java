package com.common.seq.common.auth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.common.seq.common.Constants.JWTException;
import com.common.seq.data.dto.RespErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401(인증 실패)

        String tokenVaildResult = (String)request.getAttribute("TokenVaildResult");
        String newAccessToken = (String)request.getAttribute("newAccessToken");

        log.info("[JWTAuthenticationEntryPoint] {}", tokenVaildResult);

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType("jwt Unauthorized")
                                    .code("401")
                                    .message(tokenVaildResult)
                                    .build();
        
        if (newAccessToken != null) {
            respErrorDto.setErrorType(JWTException.EXPIRED_JWT.toString());
            respErrorDto.setMessage(newAccessToken);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(respErrorDto);
        
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonStr);
        
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}