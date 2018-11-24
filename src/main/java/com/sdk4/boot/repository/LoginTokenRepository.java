package com.sdk4.boot.repository;

import com.sdk4.boot.domain.LoginToken;
import com.sdk4.boot.enums.UserTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sh
 */
@Transactional
@Repository("BootLoginTokenRepository")
public interface LoginTokenRepository extends JpaRepository<LoginToken, String>, JpaSpecificationExecutor<LoginToken> {

    /**
     * 删除用户所有 Token
     */
    @Modifying
    @Query("delete from BootLoginToken where type=:type and userId=:userId")
    int deleteUserToken(@Param("type") UserTypeEnum type, @Param("userId") String userId);

}
