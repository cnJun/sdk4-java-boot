package com.sdk4.boot.trace;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sh
 */
@Slf4j
public class Eye {
    protected static final ThreadLocal<List<LocalContext_inner>> localStack = new ThreadLocal();

    public static void startTrace(String traceId, String rpcId, String traceName) {
        if (traceName == null) {
            return;
        }
        RpcContext_inner ctx = RpcContext_inner.get();
        if (ctx != null && ctx.traceId != null) {
            if (!ctx.traceId.equals(traceId) || !traceName.equals(ctx.traceName)) {
                log.warn("duplicated startTrace detected, overrided {} ({}) to {} ({})", ctx.traceId, ctx.traceName, traceId, traceName);
                endTrace(false);
            } else {
                return;
            }
        }
        try {
            ctx = new RpcContext_inner(traceId, rpcId);
            RpcContext_inner.set(ctx);
            ctx.startTrace(traceName);
            localStack.set(new ArrayList<>());
        } catch (Throwable re) {
            log.error("startTrace error", re);
        }
    }

    public static void startLocal(String serviceName, String methodName) {
        try {
            RpcContext_inner ctx = RpcContext_inner.get();
            if (null == ctx) {
                return;
            }
            RpcContext_inner cloneCtx = ctx.cloneInstance();
            RpcContext_inner.set(cloneCtx);
            LocalContext_inner localCtx = cloneCtx.startLocal(serviceName, methodName);
            localStack.get().add(localCtx);
        } catch (Throwable re) {
            log.error("startLocal error", re);
        }
    }

    public static void endLocal() {
        try {
            RpcContext_inner ctx = RpcContext_inner.get();
            if (null == ctx) {
                return;
            }
            ctx.endLocal();
        } catch (Throwable re) {
            log.error("endLocal error", re);
        }
    }

    public static long getCost() {
        RpcContext_inner root = RpcContext_inner.get();
        if (null == root) {
            return 0;
        }
        return System.currentTimeMillis() - root.getStartTime();
    }

    public static String endTrace(boolean returnStackLog) {
        String stackLog = "";

        try {
            RpcContext_inner root = RpcContext_inner.get();
            if (null == root) {
                return "";
            }
            while (null != root.parentRpc) {
                root = root.parentRpc;
            }
            root.endTrace();

            if (returnStackLog) {
                stackLog = getStackLog(root);
            }

            commitRpcContext(root);
            localStack.set(null);
        } catch (Throwable re) {
            log.error("endTrace error", re);
        } finally {
            clearRpcContext();
        }

        return stackLog;
    }

    public static void clearRpcContext() {
        RpcContext_inner.set(null);
    }

    public static String getStackLog(RpcContext_inner ctx) {
        StringBuilder logStr = new StringBuilder();
        logStr.append(new DateTime(ctx.startTime).toString("[yyyy-MM-dd HH:mm:ss.SSS]"));
        logStr.append(ctx.getTraceName());
        List<LocalContext_inner> localCtxList = localStack.get();
        for (LocalContext_inner localCtx : localCtxList) {
            logStr.append('\n');
            logStr.append(localCtx.getLocalId());
            logStr.append('#');
            logStr.append(localCtx.getServiceName());
            logStr.append('#');
            logStr.append(localCtx.getMethodName());
            logStr.append('#');
            logStr.append(localCtx.getCost());
            logStr.append("ms");
        }
        return logStr.toString();
    }

    public static void commitRpcContext(RpcContext_inner ctx) {
    }

    static void commitLocalContext(LocalContext_inner ctx) {
    }
}
