package com.common.seq.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "ShortenUrl Response")
public class RespShortenUrlDto {
    
    private String orgUrl;
    
    private String shortenUrl;
}
