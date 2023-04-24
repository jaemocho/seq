package com.common.seq.service.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dao.shop.MemberDAO;
import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.data.dto.shop.RespMemberDto;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.service.shop.impl.MemberServiceImpl;

@ExtendWith(SpringExtension.class)
public class MemberServiceUnitTest {
    
    @Mock
    private MemberDAO memberDAO;

    @InjectMocks
    private MemberServiceImpl memberService;

    private List<Member> initMemberData() {


        List<Member> members = new ArrayList<Member>();

        Member member1 = Member.builder()
                            .id("member1")
                            .address("수원")
                            .phoneNumber("0101112222")
                            .build();

        Member member2 = Member.builder()
                            .id("member2")
                            .address("서울")
                            .phoneNumber("0101113333")
                            .build();        

        Member member3 = Member.builder()
                            .id("member3")
                            .address("대전")
                            .phoneNumber("0101114444")
                            .build();                                

        members.add(member1);
        members.add(member2);
        members.add(member3);

        return members;
    }

    @Test
    public void addMember_test() throws ShopException {

        //given 
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .id("jjm")
                                            .address("수원")
                                            .phoneNumber("010333322222")
                                            .build();

        
        Member member = Member.builder()
                            .id("jjm")
                            .address("수원")
                            .phoneNumber("010333322222")
                            .build();

        when(memberDAO.save(any(Member.class))).thenReturn(member);

        //when 
        Member returnMember = memberService.addMember(reqMemberDto);

        //then
        assertEquals("jjm", returnMember.getId());
                                            
    }
    
    @Test
    public void getAllMember_test(){

        // given
        List<Member> members = initMemberData();

        when(memberDAO.findAll()).thenReturn(members);

        // when 
        List<RespMemberDto> respMemberDtos = memberService.getAllMember();

        // then
        assertEquals(3, respMemberDtos.size());

    }

    @Test
    public void getMemberById_test() throws ShopException {

        // given
        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberDAO.findById(member.getId())).thenReturn(member);

        // when
        RespMemberDto respMemberDto = memberService.getMemberById(member.getId());

        // then
        assertEquals(member.getId(), respMemberDto.getId());
    }

    @Test
    public void updateMember_test() throws ShopException {

        // given
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .phoneNumber("01122223333")
                                            .address("제주도")
                                            .build();

        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberDAO.findById(member.getId())).thenReturn(member);

        // when
        memberService.updateMember(member.getId(), reqMemberDto);

        // then
        assertEquals("01122223333", member.getPhoneNumber());
        assertEquals("제주도", member.getAddress());

    }

}
