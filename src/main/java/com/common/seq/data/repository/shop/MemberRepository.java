package com.common.seq.data.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.shop.Member;

public interface MemberRepository extends JpaRepository<Member, String>{
    
}
