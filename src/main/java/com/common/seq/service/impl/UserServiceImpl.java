package com.common.seq.service.impl;

import java.time.LocalDateTime;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.auth.JWTProvider;
import com.common.seq.data.dao.UserDAO;
import com.common.seq.data.dto.ReqUserDto;
import com.common.seq.data.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService {
    
    private final UserDAO userDAO;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JWTProvider jwtProvider;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userDAO.getUserByEmail(username);
        
        if ( user == null ) {
            log.info("{} ", username);
            throw new UsernameNotFoundException("Not found account.[ " + username + " ]");
        }

        user.setLastLoginTime(LocalDateTime.now());

        return user;
    }

    @Transactional
    public void joinUser(ReqUserDto reqUserDto) {
        
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        userDAO.save(User.builder()
                        .pwd(passwordEncoder.encode(reqUserDto.getPwd()))
                        .email(reqUserDto.getEmail())
                        .build());
    }

    public String createToken(ReqUserDto reqUserDto) {
        
        loadUserByUsername(reqUserDto.getEmail());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(reqUserDto.getEmail(), reqUserDto.getPwd());

        // 인증매니저를 빌드한 후 인증 진행
        // authenticate 수행 시 loadUserByUsername 실행 
        // authenticationToken 의 user/password 정보로 인증을 진행 문제 없으면 authentication 을 반환 
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtProvider.createToken(authentication);

    }
    
}
