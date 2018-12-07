package com.sdk4.boot.config;

import com.sdk4.boot.db.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务
 *
 * @author sh
 */
public class TransactionManagerConfigration {
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager);
    }
}
