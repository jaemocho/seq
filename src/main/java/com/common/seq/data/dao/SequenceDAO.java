package com.common.seq.data.dao;

import com.common.seq.data.entity.Sequence;

public interface SequenceDAO {
    
    public Sequence getSequence(String date);

    public Sequence getSequenceForUpdate(String date);

    public Sequence saveSequence(Sequence sequence);
}
