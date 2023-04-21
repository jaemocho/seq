package com.common.seq.data.dao.shop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.shop.MemberDAO;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.repository.shop.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {
    
    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(String id){
        return memberRepository.findById(id).orElse(null);
    }

    public void delete(Member member){
        memberRepository.delete(member);
    }

}
