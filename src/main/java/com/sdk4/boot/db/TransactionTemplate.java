package com.sdk4.boot.db;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;

/**
 * 事务模板
 *
 * @author sh
 */
public class TransactionTemplate extends org.springframework.transaction.support.TransactionTemplate {
    public TransactionTemplate() {
        super();
    }

    public TransactionTemplate(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    @Override
    public Object execute(TransactionCallback action) throws TransactionException {
        Object result;

        try {
            result = super.execute(action);
        } catch (Throwable t) {
            result = t;
        }

        return result;
    }

}
