package com.sdk4.boot.trace;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sh
 */
@Data
public class RpcContext_inner extends AbstractContext {
    protected static final ThreadLocal<RpcContext_inner> threadLocal = new ThreadLocal();
    protected final RpcContext_inner parentRpc;
    protected final AtomicInteger childRpcIdx;
    protected LocalContext_inner localContext;

    public RpcContext_inner(String traceId, String rpcId) {
        this(traceId, rpcId, null);
    }

    public RpcContext_inner(String traceId, String rpcId, RpcContext_inner parentRpc) {
        this(traceId, rpcId, parentRpc, new AtomicInteger(0), new AtomicInteger(0));
    }

    public RpcContext_inner(String traceId, String rpcId, RpcContext_inner parentRpc, AtomicInteger localIdx) {
        this(traceId, rpcId, parentRpc, new AtomicInteger(0), localIdx);
    }

    public RpcContext_inner(String traceId, String rpcId, RpcContext_inner parentRpc, AtomicInteger childRpcIdx, AtomicInteger localIdx) {
        super(traceId, rpcId, localIdx);
        this.parentRpc = parentRpc;
        this.childRpcIdx = childRpcIdx;
    }

    protected RpcContext_inner cloneInstance() {
        RpcContext_inner clone = new RpcContext_inner(this.traceId, getRpcId(), this.parentRpc, this.childRpcIdx, this.localIdx);

        clone.traceName = this.traceName;
        clone.serviceName = this.serviceName;
        clone.methodName = this.methodName;
        clone.resultCode = this.resultCode;
        clone.callBackMsg = this.callBackMsg;
        clone.startTime = this.startTime;
        clone.logTime = this.logTime;
        clone.localId = this.localId;
        clone.localContext = this.localContext;

        return clone;
    }

    public LocalContext_inner startLocal(String serviceName, String methodName) {
        LocalContext_inner tmpContext = getCurrentLocalContext();

        LocalContext_inner localCtx = new LocalContext_inner(this.traceId, getRpcId() + "." + this.childRpcIdx.get());
        localCtx.startTime = System.currentTimeMillis();
        localCtx.serviceName = serviceName;
        localCtx.methodName = methodName;
        localCtx.localId = nextLocalId(tmpContext);
        localCtx.localParent = tmpContext;

        this.localContext = localCtx;

        return localCtx;
    }

    public void endLocal() {
        LocalContext_inner tmpContext = this.localContext;
        if ((null == tmpContext) || (tmpContext.isEnd)) {
            return;
        }

        this.localContext = tmpContext.localParent;

        tmpContext.isEnd = true;
        tmpContext.logTime = System.currentTimeMillis();

        tmpContext.cost = (int) (tmpContext.logTime - tmpContext.startTime);

        Eye.commitLocalContext(tmpContext);
    }

    LocalContext_inner getCurrentLocalContext() {
        LocalContext_inner tmpContext = this.localContext;
        while (null != tmpContext && tmpContext.isEnd) {
            tmpContext = tmpContext.localParent;
        }
        this.localContext = tmpContext;
        return tmpContext;
    }

    private String nextLocalId(LocalContext_inner tmpContext) {
        if (tmpContext == null) {
            return "0." + this.localIdx.incrementAndGet();
        }
        return tmpContext.localId + "." + tmpContext.localIdx.incrementAndGet();
    }

    public void startTrace(String traceName) {
        this.startTime = System.currentTimeMillis();
        this.traceName = traceName;
    }

    public void endTrace() {
        this.logTime = System.currentTimeMillis();
        this.cost = (int) (this.logTime - this.startTime);
    }

    public static void set(RpcContext_inner ctx) {
        threadLocal.set(ctx);
    }

    public static RpcContext_inner get() {
        return threadLocal.get();
    }
}
