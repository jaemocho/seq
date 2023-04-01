package com.common.seq.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.common.exception.SequenceException;
import com.common.seq.data.dto.naver.RespShortenUrl;
import com.common.seq.service.impl.ShortenUrlServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 단위테스트(Service 관련된 것들만 메모리에 )
 */

@Slf4j
@ExtendWith(SpringExtension.class)
public class ShortenUrlServiceTest {
    @InjectMocks
	private ShortenUrlServiceImpl shortenUrlService;
    
	@Test
    public void requestShortenUrl() throws SequenceException {
        
        String originUrl = "www.naver.com";

        ResponseEntity<RespShortenUrl> responseEntity
                    = shortenUrlService.requestShortenUrl(originUrl);
    
        RespShortenUrl su=  responseEntity.getBody();
        assertNotNull(su);

        if (su != null) {
            assertEquals("200", su.getCode());
            log.info("{} {} {}"
                , su.getCode()
                , su.getResult().getOrgUrl()
                , su.getResult().getUrl());
        };

        
        
    }

 
}
