package com.common.seq.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.data.dao.SequenceDAO;
import com.common.seq.data.entity.Sequence;
import com.common.seq.service.impl.SequenceServiceImpl;

/**
 * 단위테스트(Service 관련된 것들만 메모리에 )
 */

@ExtendWith(SpringExtension.class)
public class SequenceServiceUnitTest {

    @Mock 
	private SequenceDAO sequenceDAO;
    
    @InjectMocks
	private SequenceServiceImpl sequenceService;

    @Test
    public void get_test() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));

        Sequence sequence = new Sequence(1L,3L, date);

        // 동작 지정
        when(sequenceDAO.getSequence(date)).thenReturn(sequence);

        // test 
        Sequence seq = sequenceService.get();

        // then
        assertEquals(sequence, seq);
    }

    @Test
    public void update_test() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date(System.currentTimeMillis()));

        Sequence sequence = new Sequence(1L,3L, date);

        // 동작 지정
        when(sequenceDAO.getSequenceForUpdate(date)).thenReturn(sequence);

        // test 
        Sequence seq = sequenceService.update();

        // then (1 증가 확인)
        assertEquals(4, seq.getSeq());
    }
}
