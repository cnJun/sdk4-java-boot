package com.sdk4.boot.trace;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sh
 */
public class Tracer {

    public static final void start(String traceId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String url = httpRequest.getRequestURL().toString();
        String rpcId = httpRequest.getHeader("TraceEye-RpcId");
        Eye.startTrace(traceId, rpcId == null ? "0" : rpcId, url);
    }

    public static final long getCost() {
        return Eye.getCost();
    }

    public static final String end(boolean returnStackLog) {
        return Eye.endTrace(returnStackLog);
    }
}
