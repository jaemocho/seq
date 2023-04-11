package com.common.seq.data.dao;

import com.common.seq.data.dto.TokenDto;
import com.common.seq.data.entity.RefreshToken;

public interface RefreshTokenDAO {
    public void saveToken(TokenDto tokenDto);

    public RefreshToken getRefreshToken(String refreshToken);
}
