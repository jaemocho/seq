package com.common.seq.data.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.RefreshToken;

public class RefreshTokenRepositoryUnitTest extends BaseRepositoryTest{
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    public void findByRefreshToken_test() {
                
        //given
        RefreshToken refreshToken = RefreshToken.builder()
                                    .refreshToken("refresh_token")
                                    .accessToken("access_token")
                                    .build();

        //when
        RefreshToken refreshTokenEntity = refreshTokenRepository.save(refreshToken);

        //then 
        assertEquals("access_token", refreshTokenEntity.getAccessToken());

        RefreshToken findRefreshTokenEntity = refreshTokenRepository.findByRefreshToken("refresh_token");

        assertTrue(findRefreshTokenEntity.equals(refreshTokenEntity));
    }
}
