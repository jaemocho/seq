package com.common.seq.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.common.seq.data.dao.SequenceDAO;
import com.common.seq.data.entity.Sequence;
import com.common.seq.service.SequenceService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class SequenceServiceImpl implements SequenceService{
    
    @Qualifier("SequenceDAOImpl")
    private final SequenceDAO sequenceDAO;
    
    final Long seqMaxVal = 9999999999L;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public Sequence save(Sequence sequence){
        return sequenceDAO.saveSequence(sequence);
    }

    @Transactional
    public Sequence update(){
        
        String date = formatter.format(new Date(System.currentTimeMillis()));
        
        Sequence sequence = sequenceDAO.getSequenceForUpdate(date);

        if ( sequence == null ) {
            sequence = save(new Sequence(0L, 0L, date));
        }
        // log.error(sequence.getId() +" "+ sequence.getSeq() + " " + seqMaxVal.equals(sequence.getSeq()) );  
        
        if ( seqMaxVal.equals(sequence.getSeq())) {
            sequence.updateSeq(1L) ;   
        } else {
            sequence.updateSeq(sequence.getSeq()+1);
        }
        return sequence;
    }

    @Transactional
    public Sequence get(){
        
        String date = formatter.format(new Date(System.currentTimeMillis()));
        
        Sequence sequence = sequenceDAO.getSequence(date);
        if (sequence == null) {
            sequence = save(new Sequence(0L, 0L, date));
        }
        return sequence;
    }

}
