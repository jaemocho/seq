package com.common.seq.data.dao.impl;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.UserDAO;
import com.common.seq.data.entity.User;
import com.common.seq.data.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDAOImpl implements UserDAO{

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
}
