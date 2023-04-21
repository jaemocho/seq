package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dao.shop.MemberDAO;
import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.data.dto.shop.RespMemberDto;
import com.common.seq.data.entity.shop.Member;
import com.common.seq.service.shop.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    
    public final MemberDAO memberDAO;
    
    @Transactional
    public Member addMember(ReqMemberDto reqMemberDto){
        
        Member member = Member.builder()
                            .id(reqMemberDto.getId()) 
                            .address(reqMemberDto.getAddress())
                            .phoneNumber(reqMemberDto.getPhoneNumber())
                            .build();

        return memberDAO.save(member);
    }

    @Transactional
    public void removeMember(String id) {

        Member member = memberDAO.findById(id);

        if( member == null ){
            // exception
            return ;
        }

        // member가 가지고 있는 order list 확인
        // order list가 있는 경우 orderitem을 확인 한 후 삭제
        // item 의 remainQty update 
        if ( member.getOrders() != null && member.getOrders().size() > 0 ) {

        }

        // member 가 삭제 될 때 order 까지 삭제 되면 
        // 주문 수량을 되돌려 놓지 못하니까 orphanRemoval 없이 
        memberDAO.delete(member);

    }

    @Transactional(readOnly = true)
    public List<RespMemberDto> getAllMember() {

        List<Member> members = memberDAO.findAll();

        List<RespMemberDto> respMemberDtos = new ArrayList<RespMemberDto>();

        for(Member m : members) {
            respMemberDtos.add(entityToRespDto(m));
        }

        return respMemberDtos;
    }

    @Transactional(readOnly = true)
    public RespMemberDto getMemberById(String id) {

        Member member = memberDAO.findById(id);

        return entityToRespDto(member);
    }

    @Transactional
    public void updateMember(String id, ReqMemberDto reqMemberDto) {

        // 사용자 정보 변경은 lock 없이 
        Member member = memberDAO.findById(id);

        if ( member == null ) {
            return;
        }

        member.updateMember(reqMemberDto.getAddress(), reqMemberDto.getPhoneNumber());

    }

    private RespMemberDto entityToRespDto(Member m) {
        return RespMemberDto.builder()
                        .id(m.getId())
                        .address(m.getAddress())
                        .phoneNumber(m.getPhoneNumber())
                        .build();
    }


}
