package com.common.seq.service.shop;

import java.util.List;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.data.dto.shop.RespMemberDto;
import com.common.seq.data.entity.shop.Member;

public interface MemberService {
    
    public Member addMember(ReqMemberDto reqMemberDto) throws ShopException;

    public void removeMember(String id) throws ShopException;

    public List<RespMemberDto> getAllMember();

    public RespMemberDto getMemberById(String id) throws ShopException;

    public void updateMember(String id, ReqMemberDto reqMemberDto) throws ShopException;

    public Member getMember(String memberId);

}
