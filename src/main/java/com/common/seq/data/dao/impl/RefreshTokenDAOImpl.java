package com.common.seq.data.dao.impl;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.RefreshTokenDAO;
import com.common.seq.data.dto.TokenDto;
import com.common.seq.data.entity.RefreshToken;
import com.common.seq.data.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RefreshTokenDAOImpl implements RefreshTokenDAO {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveToken(TokenDto tokenDto) {
        refreshTokenRepository.save(RefreshToken.builder()
                                        .accessToken(tokenDto.getAccessToken())
                                        .refreshToken(tokenDto.getRefreshToken())
                                        .build());
    }

    @Override
    public RefreshToken getRefreshToken(String refreshToken) {

        return refreshTokenRepository.findByRefreshToken(refreshToken);

    }
    
}
