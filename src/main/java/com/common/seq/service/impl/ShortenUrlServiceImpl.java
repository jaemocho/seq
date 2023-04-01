package com.common.seq.service.impl;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.common.seq.data.dto.naver.RespShortenUrl;
import com.common.seq.service.ShortenUrlService;

@Service
public class ShortenUrlServiceImpl implements ShortenUrlService {
    
    @Value("${naver.api.client.id}")
    private String CLIENT_ID;

    @Value("${naver.api.client.secret}")
    private String CLIENT_SECRET;

    public ResponseEntity<RespShortenUrl> requestShortenUrl(String originUrl)  {

        URI uri = UriComponentsBuilder
        .fromUriString("https://openapi.naver.com")
        .path("/v1/util/shorturl")
        .queryParam("url", originUrl)
        .encode()
        .build()
        .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        // body + header, 요청 spec에 body 는 없어서 header만 
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate restTemplete = new RestTemplate();

        ResponseEntity<RespShortenUrl> responseEntity = restTemplete.exchange(uri, HttpMethod.GET, entity, RespShortenUrl.class);
  
        return responseEntity;
    }

}
