package com.common.seq.data.dao.shop;

import java.util.List;

import com.common.seq.data.entity.shop.Member;

public interface MemberDAO {
    
    public Member save(Member member);
        
    public List<Member> findAll();

    public Member findById(String id);

    public void delete(Member member);

}
