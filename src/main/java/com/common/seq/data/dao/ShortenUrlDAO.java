package com.common.seq.data.dao;

import com.common.seq.data.entity.ShortenUrl;

public interface ShortenUrlDAO {
    
    public ShortenUrl save(ShortenUrl shortenUrl);

    public ShortenUrl findByOrgUrl(String orgUrl);
}
