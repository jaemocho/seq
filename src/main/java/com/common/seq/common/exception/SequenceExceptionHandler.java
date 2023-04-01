package com.common.seq.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class SequenceExceptionHandler {
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(HttpStatusCodeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = SequenceException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(SequenceException e) {
        HttpHeaders responseHeaders = new HttpHeaders();

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        Map<String, String> map = new HashMap<>();
        map.put("error type", e.getHttpStatusType());
        map.put("error code",
            Integer.toString(e.getHttpStatusCode())); // Map<String, Object>로 설정하면 toString 불필요
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, responseHeaders, e.getHttpStatus());
    }
}
