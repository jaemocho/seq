package com.common.seq.data.dto;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "ShortenUrl Response")
@RedisHash(value = "shortenUrl", timeToLive = 60) // key 값에 prefix
public class RespShortenUrlDto implements Serializable{

    private static final long serialVersionUID = 1977936443460921608L;

    @Id
    private String orgUrl;
    
    private String shortenUrl;
}
