package com.common.seq.data.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.common.seq.data.entity.Sequence;
import jakarta.transaction.Transactional;

@Transactional
@AutoConfigureTestDatabase(replace= Replace.ANY) 
@DataJpaTest
public class SequenceRepositoryUnitTest {
    
    @Autowired
    private SequenceRepository sequenceRepository;

    @Test
    public void findByDate_test() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));
        
        Sequence sequence = new Sequence(0L, 0L, date);

        Sequence seq = sequenceRepository.save(sequence);

        assertEquals(0L, seq.getSeq());

        seq = sequenceRepository.findByDate(date);

        assertEquals(0L, seq.getSeq());
    }
}
