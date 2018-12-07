package com.sdk4.boot.repository;

import com.sdk4.boot.domain.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootSysConfigRepository")
public interface SysConfigRepository extends JpaRepository<SysConfig, String>, JpaSpecificationExecutor<SysConfig> {
}
