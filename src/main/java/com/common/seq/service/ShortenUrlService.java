package com.common.seq.service;

import com.common.seq.data.dto.RespShortenUrlDto;


public interface ShortenUrlService {
    
    public RespShortenUrlDto genShortenUrl(String originUrl);
}
