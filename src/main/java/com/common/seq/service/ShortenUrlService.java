package com.common.seq.service;

import org.springframework.http.ResponseEntity;

import com.common.seq.data.dto.naver.RespShortenUrl;

public interface ShortenUrlService {
    public ResponseEntity<RespShortenUrl> requestShortenUrl(String originUrl) ;
}
