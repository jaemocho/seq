package com.common.seq.data.dto.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespShortenUrl {
    private String message;
    private String code;
    private Result result;
    
    @Data
    public static class Result {
        private String hash;
        private String url;
        private String orgUrl;
    };
}
