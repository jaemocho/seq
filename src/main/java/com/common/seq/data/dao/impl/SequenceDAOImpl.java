package com.common.seq.data.dao.impl;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.SequenceDAO;
import com.common.seq.data.entity.Sequence;
import com.common.seq.data.repository.SequenceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SequenceDAOImpl implements SequenceDAO{
    private final SequenceRepository sequenceRepository;

    public Sequence getSequence(String date){
        return sequenceRepository.findByDate(date);
    }

    public Sequence getSequenceForUpdate(String date){
        return sequenceRepository.findByDateForUpdate(date);
    }

    public Sequence saveSequence(Sequence sequence) {
        return sequenceRepository.save(sequence);    
    }
}
