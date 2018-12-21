package com.sdk4.boot.trace;

/**
 * @author sh
 */
public class LocalContext_inner extends AbstractContext {
    LocalContext_inner localParent;
    volatile boolean isEnd;

    LocalContext_inner(String traceId, String rpcId) {
        super(traceId, rpcId);
    }

    LocalContext_inner(String traceId, String rpcId, String localId) {
        super(traceId, rpcId);
        this.localId = localId;
    }
}
