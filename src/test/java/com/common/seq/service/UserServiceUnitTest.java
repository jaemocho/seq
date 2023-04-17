package com.common.seq.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.data.dao.UserDAO;
import com.common.seq.data.dto.ReqUserDto;
import com.common.seq.data.entity.User;
import com.common.seq.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
	private UserServiceImpl userServiceImpl;
    
    @Test
    public void joinUser_test() {
        User user = User.builder()
                        .email("user@gmail.com")
                        .pwd("password")
                        .build();
        
        ReqUserDto reqUserDto = ReqUserDto.builder()
                                    .email("user@gmail.com")
                                    .pwd("password")
                                    .build();

        when(userDAO.save(user)).thenReturn(user);

        userServiceImpl.joinUser(reqUserDto);

        assertEquals("user@gmail.com", user.getUsername());
        assertEquals("password", user.getPassword());
    }
}
