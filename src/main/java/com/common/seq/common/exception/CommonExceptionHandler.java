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
public class CommonExceptionHandler {
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(httpStatus.getReasonPhrase()
                                    , "400", "예상치 못한 에러가 발생 하였습니다. 관리자에게 문의해 주세요");                                    
        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(AuthenticationException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(httpStatus.getReasonPhrase()
                                    , "401", e.getMessage());
        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }
    

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(MethodArgumentNotValidException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = getFieldError(e);
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(httpStatus.getReasonPhrase()
                                                    , "400", message);
        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(UsernameNotFoundException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(httpStatus.getReasonPhrase()
                                                    , "400", e.getMessage());
        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(HttpStatusCodeException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(httpStatus.getReasonPhrase()
                                                , "400", e.getMessage());
        return new ResponseEntity<>(respErrorDto, responseHeaders, httpStatus);
    }

    @ExceptionHandler(value = SequenceException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(SequenceException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(e.getHttpStatusType()
                                        , Integer.toString(e.getHttpStatusCode()), e.getMessage());
        return new ResponseEntity<>(respErrorDto, responseHeaders, e.getHttpStatus());
    }

    @ExceptionHandler(value = ShopException.class)
    public ResponseEntity<RespErrorDto> ExceptionHandler(ShopException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        log.error("Exception {}, {}", e.getCause(), e.getMessage());  
        RespErrorDto respErrorDto = createRespErroDto(e.getHttpStatusType()
                                        ,Integer.toString(e.getHttpStatusCode()), e.getMessage());
        return new ResponseEntity<>(respErrorDto, responseHeaders, e.getHttpStatus());
    }

    private RespErrorDto createRespErroDto(String errorType, String code, String message){
        RespErrorDto respErrorDto = RespErrorDto.builder()
                                    .errorType(errorType)
                                    .code(code)
                                    .message(message)
                                    .build();

        return respErrorDto;                                    
    }

    private String getFieldError(MethodArgumentNotValidException e) {
        StringBuffer sb = new StringBuffer();
        for ( FieldError fe : e.getBindingResult().getFieldErrors()) {
                sb.append(fe.getField())
                    .append(":")
                    .append(fe.getDefaultMessage());
        }

        return sb.toString();
    }
}
