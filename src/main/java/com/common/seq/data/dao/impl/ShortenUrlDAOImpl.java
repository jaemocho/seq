package com.common.seq.data.dao.impl;

import org.springframework.stereotype.Component;

import com.common.seq.data.dao.ShortenUrlDAO;
import com.common.seq.data.entity.ShortenUrl;
import com.common.seq.data.repository.ShortenUrlRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShortenUrlDAOImpl implements ShortenUrlDAO {

    private final ShortenUrlRepository shortenUrlRepository;

    @Override
    public ShortenUrl save(ShortenUrl shortenUrl) {
        return shortenUrlRepository.save(shortenUrl);
    }

    @Override
    public ShortenUrl findByOrgUrl(String orgUrl) {
        return shortenUrlRepository.findByOrgUrl(orgUrl);
    }
    
}
