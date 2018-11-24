package com.sdk4.boot.repository;

import com.sdk4.boot.domain.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootApiLogRepository")
public interface ApiLogRepository extends JpaRepository<ApiLog, String>, JpaSpecificationExecutor<ApiLog> {
}
