package com.sdk4.boot.repository;

import com.sdk4.boot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootUserRepository")
public interface UserRepository extends BootUserRepository, JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
