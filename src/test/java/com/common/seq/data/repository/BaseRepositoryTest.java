package com.common.seq.data.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.config.ProfileManager;
import com.common.seq.config.env.impl.DevConfiguration;

@Transactional
@AutoConfigureTestDatabase(replace= Replace.ANY) 
@DataJpaTest
public class BaseRepositoryTest {
    @MockBean
    private DevConfiguration devConfiguration;

    @MockBean
    private ProfileManager profileManager;
    
}
