package com.common.seq.data.repository.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.common.seq.data.entity.shop.Member;
import com.common.seq.data.repository.BaseRepositoryTest;

public class MemberRepositoryUnitTest extends BaseRepositoryTest {
    
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void member_test(){

        // 초기 member date insert 
        Member member1 = new Member();
        member1.setId("memberA");
        member1.setAddress("서울 여기 저기");
        member1.setPhoneNumber("010 1234 5678");

        Member member2 = new Member();
        member2.setId("memberB");
        member2.setAddress("수원 여기 저기");
        member2.setPhoneNumber("010 1234 7890");

        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.flush();

        // 전체 member 조회 테스트 
        List<Member> members = memberRepository.findAll(); 
        assertEquals(2, members.size());
        
        // memberA 조회 테스트 
        Member findMember = memberRepository.findById("memberA").orElse(null);
        assertEquals("서울 여기 저기", findMember.getAddress());

        // memberA 정보 update 테스트 
        findMember.setAddress("부산 여기 저기");
        memberRepository.flush();
        findMember = memberRepository.findById("memberA").orElse(null);
        assertEquals("부산 여기 저기", findMember.getAddress());

        memberRepository.delete(findMember);
        memberRepository.flush();
        assertEquals(1 ,memberRepository.findAll().size());
    }
}
