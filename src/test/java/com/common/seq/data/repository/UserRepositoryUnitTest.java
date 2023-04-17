package com.common.seq.data.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.common.seq.data.entity.User;


public class UserRepositoryUnitTest extends BaseRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail_test() {
        //given
        User user = User.builder()
                    .email("test@hanmail.net")
                    .pwd("abcd")
                    .build();

        //when
        User userEntity = userRepository.save(user);

        //then 
        assertEquals("test@hanmail.net", userEntity.getEmail());

        User findUserEntity = userRepository.findByEmail("test@hanmail.net");

        assertTrue(userEntity.equals(findUserEntity));
    }
}
