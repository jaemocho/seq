package com.common.seq.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>  {
    
    public RefreshToken findByRefreshToken(String refreshToken);
}
