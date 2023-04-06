package com.common.seq.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{  
    
    public User findByEmail(String email);
}
