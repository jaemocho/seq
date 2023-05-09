package com.common.seq.data.repository.redis;

import org.springframework.data.repository.CrudRepository;

import com.common.seq.data.dto.RespShortenUrlDto;

public interface ShortenUrlRedisRepository extends CrudRepository<RespShortenUrlDto, String> {
    
}