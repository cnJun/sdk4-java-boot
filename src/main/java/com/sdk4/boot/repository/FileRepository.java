package com.sdk4.boot.repository;

import com.sdk4.boot.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootFileRepository")
public interface FileRepository extends JpaRepository<File, String>, JpaSpecificationExecutor<File> {
}
