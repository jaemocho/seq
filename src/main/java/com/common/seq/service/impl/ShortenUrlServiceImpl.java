package com.common.seq.service.impl;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.common.seq.data.dao.ShortenUrlDAO;
import com.common.seq.data.dto.RespShortenUrlDto;
import com.common.seq.data.dto.naver.RespShortenUrl;
import com.common.seq.data.entity.ShortenUrl;
import com.common.seq.data.repository.redis.ShortenUrlRedisRepository;
import com.common.seq.service.ShortenUrlService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShortenUrlServiceImpl implements ShortenUrlService {

    private final ShortenUrlDAO shortenUrlDAO;

    private final ShortenUrlRedisRepository shortenUrlRedisRepository;

    private ResponseEntity<RespShortenUrl> requestShortenUrl(String originUrl, String CLIENT_ID, String CLIENT_SECRET)  {

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
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

        ResponseEntity<RespShortenUrl> responseEntity 
            = restTemplete.exchange(uri, HttpMethod.GET, entity, RespShortenUrl.class);
  
        return responseEntity;
    }

    public RespShortenUrlDto genShortenUrl(String originUrl, String CLIENT_ID, String CLIENT_SECRET) {

        Optional<RespShortenUrlDto> cachedRespShortenUrlDto = shortenUrlRedisRepository.findById(originUrl);
        if ( cachedRespShortenUrlDto.isPresent()) {
            log.info("[cache] existed originUrl : {} ", originUrl);
            return cachedRespShortenUrlDto.get();
        } else {
            log.info("[cache] not existed originUrl : {} ", originUrl);
        }

        ShortenUrl shortenUrl = shortenUrlDAO.findByOrgUrl(originUrl);
        
        if (shortenUrl == null)  {
            ResponseEntity<RespShortenUrl> responseEntity = requestShortenUrl(originUrl, CLIENT_ID, CLIENT_SECRET);
            
            if (responseEntity == null || responseEntity.getBody() == null) return null;

            shortenUrl = shortenUrlDAO.save(
                ShortenUrl.builder()
                    .orgUrl(originUrl)
                    .url(responseEntity.getBody().getResult().getUrl())
                    .hash(responseEntity.getBody().getResult().getHash())
                    .build()
            );
        } 

        RespShortenUrlDto respShortenUrlDto =  RespShortenUrlDto.builder()
                                                    .shortenUrl(shortenUrl.getUrl())
                                                    .orgUrl(shortenUrl.getOrgUrl())
                                                    .build();
        // redis cache 저장
        shortenUrlRedisRepository.save(respShortenUrlDto);

        return respShortenUrlDto;
    }

}
