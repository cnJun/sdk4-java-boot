package com.sdk4.boot.repository;

import com.sdk4.boot.domain.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootSequenceRepository")
public interface SequenceRepository extends JpaRepository<Sequence, String>, JpaSpecificationExecutor<Sequence> {

    @Procedure(name = "SEQ_nextval")
    Long nextVal(@Param("seqname") String seqname);

}
