package com.sdk4.boot.trace;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sh
 */
@Data
public abstract class AbstractContext extends BaseContext {
    protected int cost = 0;
    protected long startTime = 0L;
    protected String localId = "";
    protected final AtomicInteger localIdx;

    public AbstractContext(String traceId, String rpcId) {
        this(traceId, rpcId, new AtomicInteger(0));
    }

    public AbstractContext(String traceId, String rpcId, AtomicInteger localIdx) {
        super(traceId, rpcId);
        this.localIdx = localIdx;
    }

}
