package com.sdk4.boot.db;

import com.sdk4.common.id.IdUtils;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author sh
 */
public class IdKeyUuidGenerator implements Configurable, IdentifierGenerator {
    private String type;
    private String prefix;

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
        Serializable result = null;

        if ("string".equals(type)) {
            result = this.prefix + IdUtils.fastUUID().toString();
        } else {
            throw new HibernateException("未定义类型[" + type + "]");
        }

        return result;
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        this.type = properties.getProperty("type", "string");
        this.prefix = properties.getProperty("prefix", "");
    }

}
