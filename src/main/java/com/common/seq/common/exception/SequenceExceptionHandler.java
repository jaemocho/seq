package com.common.seq.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import com.common.seq.data.dto.RespErrorDto;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class SequenceExceptionHandler {
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(httpStatus.getReasonPhrase())
                                    .code("400")
                                    .message("예상치 못한 에러가 발생 하였습니다. 관리자에게 문의해 주세요")
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(AuthenticationException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(httpStatus.getReasonPhrase())
                                    .code("401")
                                    .message(e.getMessage())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }
    

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(MethodArgumentNotValidException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        StringBuffer sb = new StringBuffer();
        
        for ( FieldError fe : e.getBindingResult().getFieldErrors()) {
            sb.append(fe.getField())
                .append(":")
                .append(fe.getDefaultMessage());
        }

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(httpStatus.getReasonPhrase())
                                    .code("400")
                                    .message(sb.toString())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(UsernameNotFoundException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(httpStatus.getReasonPhrase())
                                    .code("400")
                                    .message(e.getMessage())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(HttpStatusCodeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        
        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(httpStatus.getReasonPhrase())
                                    .code("400")
                                    .message(e.getMessage())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = SequenceException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(SequenceException e) {
        HttpHeaders responseHeaders = new HttpHeaders();

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(e.getHttpStatusType())
                                    .code(Integer.toString(e.getHttpStatusCode()))
                                    .message(e.getMessage())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, e.getHttpStatus());
    }

    @ExceptionHandler(value = ShopException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(ShopException e) {
        HttpHeaders responseHeaders = new HttpHeaders();

        log.error("Exception {}, {}", e.getCause(), e.getMessage());  

        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(e.getHttpStatusType())
                                    .code(Integer.toString(e.getHttpStatusCode()))
                                    .message(e.getMessage())
                                    .build();

        return new ResponseEntity<>(respErrorDto, responseHeaders, e.getHttpStatus());
    }
}
