package com.common.seq.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.seq.data.dto.ReqUserDto;
import com.common.seq.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
 
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody ReqUserDto reqUserDto) {
        
        userServiceImpl.joinUser(reqUserDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK); 
    }
}
