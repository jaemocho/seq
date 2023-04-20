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
        Member member1 = Member.builder()
                            .id("memberA")
                            .address("서울 여기 저기")
                            .phoneNumber("010 1234 5678")
                            .build();

        Member member2 = Member.builder()
                            .id("memberB")
                            .address("수원 여기 저기")
                            .phoneNumber("010 1234 7890")
                            .build();

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
        findMember.updateMember("부산 여기 저기", findMember.getPhoneNumber());
        memberRepository.flush();
        findMember = memberRepository.findById("memberA").orElse(null);
        assertEquals("부산 여기 저기", findMember.getAddress());

        memberRepository.delete(findMember);
        memberRepository.flush();
        assertEquals(1 ,memberRepository.findAll().size());
    }
}
