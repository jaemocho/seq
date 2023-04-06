package com.common.seq.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dao.UserDAO;
import com.common.seq.data.dto.ReqUserDto;
import com.common.seq.data.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService {
    
    private final UserDAO userDAO;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userDAO.getUserByEmail(username);
        
        if ( user == null ) throw new UsernameNotFoundException("Not found account.");

        user.setLastLoginTime(LocalDateTime.now());

        return user;
    }

    @Transactional
    public void joinUser(ReqUserDto reqUserDto) {
        
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // log.info("{} {} {}", reqUserDto.getPwd(), reqUserDto.getEmail(), passwordEncoder.encode(reqUserDto.getPwd()));
        
        userDAO.save(User.builder()
                        .pwd(passwordEncoder.encode(reqUserDto.getPwd()))
                        .email(reqUserDto.getEmail())
                        .build());
    }
    
}
