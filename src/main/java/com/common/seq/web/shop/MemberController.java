package com.common.seq.web.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqMemberDto;
import com.common.seq.data.dto.shop.RespMemberDto;
import com.common.seq.service.shop.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class MemberController {
    
    private final MemberService memberService;

    @PostMapping(path = "/member")
    public ResponseEntity<?> join(@Valid @RequestBody ReqMemberDto reqMemberDto) throws ShopException {
        
        memberService.addMember(reqMemberDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/member/{id}")
    public ResponseEntity<?> removeMember(@PathVariable("id") String id) throws ShopException {

        memberService.removeMember(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/member/{id}")
    public ResponseEntity<RespMemberDto> getMemberById(@PathVariable("id") String id) throws ShopException{

        return new ResponseEntity<RespMemberDto>(memberService.getMemberById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/member/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") String id, @Valid @RequestBody ReqMemberDto reqMemberDto) throws ShopException {

        memberService.updateMember(id, reqMemberDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/members")
    public ResponseEntity<List<RespMemberDto>> getAllMember() {

        return new ResponseEntity<List<RespMemberDto>>(memberService.getAllMember(), HttpStatus.OK);
    }
}
