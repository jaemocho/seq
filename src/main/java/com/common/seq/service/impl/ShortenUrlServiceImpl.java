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
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public RespShortenUrlDto genShortenUrl(String originUrl, String CLIENT_ID, String CLIENT_SECRET) {

        RespShortenUrlDto respShortenUrlDto = findShortenUrlInCache(originUrl);
        if (respShortenUrlDto != null) return respShortenUrlDto;

        ShortenUrl shortenUrl = findShortenUrlInDB(originUrl);
        if (shortenUrl == null)  {
            ResponseEntity<RespShortenUrl> responseEntity = requestShortenUrl(originUrl, CLIENT_ID, CLIENT_SECRET);
            shortenUrl = createShortenUrl(responseEntity);
            shortenUrl = saveShortenUrlInDB(shortenUrl);
        } 

        respShortenUrlDto =  createRespShortenUrlDto(shortenUrl);
        saveShortenUrlInCache(respShortenUrlDto);
        return respShortenUrlDto;
    }

    private RespShortenUrlDto findShortenUrlInCache(String originUrl) {
        
        Optional<RespShortenUrlDto> cachedRespShortenUrlDto = shortenUrlRedisRepository.findById(originUrl);
        if ( cachedRespShortenUrlDto.isPresent()) {
            log.info("[cache] existed originUrl : {} ", originUrl);
            return cachedRespShortenUrlDto.get();
        } else {
            log.info("[cache] not existed originUrl : {} ", originUrl);
        }

        return null;
    }

    private ShortenUrl findShortenUrlInDB(String originUrl) {
        return shortenUrlDAO.findByOrgUrl(originUrl);
    }

    private ResponseEntity<RespShortenUrl> requestShortenUrl(String originUrl, String CLIENT_ID, String CLIENT_SECRET)  {
        URI uri = createURIForNaverAPI(originUrl);
        HttpHeaders headers = makeHeaders(CLIENT_ID, CLIENT_SECRET);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        RestTemplate restTemplete = new RestTemplate();
        ResponseEntity<RespShortenUrl> responseEntity 
            = restTemplete.exchange(uri, HttpMethod.GET, entity, RespShortenUrl.class);
        return responseEntity;
    }

    private URI createURIForNaverAPI(String originUrl) {
        return UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/util/shorturl")
                .queryParam("url", originUrl)
                .encode()
                .build()
                .toUri();
    }

    private HttpHeaders makeHeaders(String CLIENT_ID, String CLIENT_SECRET){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);
        return headers;
    }

    private ShortenUrl createShortenUrl(ResponseEntity<RespShortenUrl> responseEntity) {
        return  ShortenUrl.builder()
                .orgUrl(responseEntity.getBody().getResult().getOrgUrl())
                .url(responseEntity.getBody().getResult().getUrl())
                .hash(responseEntity.getBody().getResult().getHash())
                .build();
    }

    private ShortenUrl saveShortenUrlInDB(ShortenUrl shortenUrl) {
        return shortenUrlDAO.save(shortenUrl);
    }

    private RespShortenUrlDto createRespShortenUrlDto (ShortenUrl shortenUrl){
        return RespShortenUrlDto.builder()
                            .shortenUrl(shortenUrl.getUrl())
                            .orgUrl(shortenUrl.getOrgUrl())
                            .build();
    }

    private void saveShortenUrlInCache(RespShortenUrlDto respShortenUrlDto){
        shortenUrlRedisRepository.save(respShortenUrlDto);
    }



}
