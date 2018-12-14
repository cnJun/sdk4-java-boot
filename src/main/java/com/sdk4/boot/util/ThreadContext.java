package com.sdk4.boot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sh on 2018/8/4.
 */
public abstract class ThreadContext {
    private static final Logger log = LoggerFactory.getLogger(ThreadContext.class);

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

    protected ThreadContext() {
    }

    public static Map<Object, Object> getResources() {
        if (resources.get() == null){
            return Collections.emptyMap();
        } else {
            return new HashMap<>(resources.get());
        }
    }

    public static void setResources(Map<Object, Object> newResources) {
        if (newResources.isEmpty()) {
            return;
        }
        ensureResourcesInitialized();
        resources.get().clear();
        resources.get().putAll(newResources);
    }

    private static Object getValue(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        return perThreadResources != null ? perThreadResources.get(key) : null;
    }

    private static void ensureResourcesInitialized(){
        if (resources.get() == null){
            resources.set(new HashMap<>(10));
        }
    }

    public static Object get(Object key) {
        if (log.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

        Object value = getValue(key);
        if ((value != null) && log.isTraceEnabled()) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] " + "bound to thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
        return value;
    }

    public static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        if (value == null) {
            remove(key);
            return;
        }

        ensureResourcesInitialized();
        resources.get().put(key, value);

        if (log.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" +
                    key + "] to thread " + "[" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }
    }

    public static Object remove(Object key) {
        Map<Object, Object> perThreadResources = resources.get();
        Object value = perThreadResources != null ? perThreadResources.remove(key) : null;

        if ((value != null) && log.isTraceEnabled()) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" +
                    key + "]" + "from thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

        return value;
    }

    public static void remove() {
        resources.remove();
    }

    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {
        @Override
        @SuppressWarnings({"unchecked"})
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    }
}
