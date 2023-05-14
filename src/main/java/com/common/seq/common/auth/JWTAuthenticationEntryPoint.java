package com.common.seq.common.auth;

import java.io.IOException;

import org.springframework.http.MediaType;
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
        RespErrorDto respErrorDto = createRespErrorDto(tokenVaildResult);
        setNewAccessToken(respErrorDto, newAccessToken);
        String jsonStr = dtoToJsonString(respErrorDto);
        responseResult(response, jsonStr, HttpServletResponse.SC_UNAUTHORIZED, MediaType.APPLICATION_JSON_VALUE);
    }

    private RespErrorDto createRespErrorDto(String message) {
        RespErrorDto respErrorDto = RespErrorDto.builder()
                                            .errorType("jwt Unauthorized")
                                            .code("401")
                                            .message(message)
                                            .build();
        return respErrorDto;                                            
    }

    private void setNewAccessToken(RespErrorDto respErrorDto, String newAccessToken) {
        if (newAccessToken != null) {
            respErrorDto.setErrorType(JWTException.EXPIRED_JWT.toString());
            respErrorDto.setMessage(newAccessToken);
        }
    }

    private String dtoToJsonString(Object object) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(object);
        return jsonStr;
    }

    private void responseResult(HttpServletResponse response, String body, int status, String contentType) throws IOException{
        response.setContentType(contentType);
        response.setStatus(status);
        response.getWriter().write(body);
    }

}