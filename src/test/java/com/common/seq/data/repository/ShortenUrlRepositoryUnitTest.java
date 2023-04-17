package com.common.seq.data.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.common.seq.data.entity.ShortenUrl;


public class ShortenUrlRepositoryUnitTest extends BaseRepositoryTest {

    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    @Test
    public void findByOrgUrl_test() {
        //given
        ShortenUrl shortenUrl = ShortenUrl.builder()
                                    .hash("hash")
                                    .url("https://me2.do/example")
                                    .orgUrl("www.naver.com")
                                    .build();

        //when
        ShortenUrl shortenUrlEntity = shortenUrlRepository.save(shortenUrl);

        //then 
        assertEquals("www.naver.com", shortenUrlEntity.getOrgUrl());

        ShortenUrl findShortenUrlEntity = shortenUrlRepository.findByOrgUrl("www.naver.com");

        assertTrue(findShortenUrlEntity.equals(shortenUrlEntity));

    }
}
