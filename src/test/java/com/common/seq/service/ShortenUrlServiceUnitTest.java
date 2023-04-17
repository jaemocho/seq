package com.common.seq.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.common.exception.SequenceException;
import com.common.seq.data.dao.ShortenUrlDAO;
import com.common.seq.data.dto.RespShortenUrlDto;
import com.common.seq.data.entity.ShortenUrl;
import com.common.seq.service.impl.ShortenUrlServiceImpl;



/**
 * 단위테스트(Service 관련된 것들만 메모리에 )
 */


@ExtendWith(SpringExtension.class)
public class ShortenUrlServiceUnitTest {
    
    @Mock
    private ShortenUrlDAO shortenUrlDAO;

    @InjectMocks
	private ShortenUrlServiceImpl shortenUrlService;

	@Test
    public void requestShortenUrl() throws SequenceException {
        
        String originUrl = "www.naver.com";

        ShortenUrl shortenUrl = ShortenUrl.builder()
                                    .hash("hash")
                                    .url("https://me2.do/example")
                                    .orgUrl("www.naver.com")
                                    .build();

        when(shortenUrlDAO.findByOrgUrl(originUrl)).thenReturn(shortenUrl);
        
        RespShortenUrlDto responseEntity
                    = shortenUrlService.genShortenUrl(originUrl, "CLIENT_ID", "CLIENT_SECRET");
    
        assertEquals("https://me2.do/example", responseEntity.getShortenUrl());
        assertEquals("www.naver.com", responseEntity.getOrgUrl());
        
    }

 
}
