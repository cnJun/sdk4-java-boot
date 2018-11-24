package com.sdk4.boot.service;

/**
 * @author sh
 */
public interface SequenceService {
    /**
     * 通用序号生成
     *
     * @param type
     */
    Long nextVal(String type);

}
