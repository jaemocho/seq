package com.common.seq.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.ShortenUrl;

public interface ShortenUrlRepository extends JpaRepository<ShortenUrl, Long>{  
    
    public ShortenUrl findByOrgUrl(String orgUrl);
}
