package com.common.seq.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.common.seq.data.entity.Sequence;

import jakarta.transaction.Transactional;

public interface SequenceRepository extends JpaRepository<Sequence, Long> {
    
    @Transactional
    @Query(value = "SELECT * FROM tb_sequence WHERE date = :date for update", nativeQuery = true)
    Sequence findByDateForUpdate(@Param("date") String date);

    @Transactional
    Sequence findByDate(@Param("date") String date);

    // @Transactional
    // @Lock(LockModeType.PESSIMISTIC_WRITE)  
    // Optional<Sequence> findById(Long id); 사용 시 
    // Hibernate: select s1_0.id,s1_0.seq from Sequence s1_0 where s1_0.id=? for update

}
