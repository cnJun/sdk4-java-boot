package com.sdk4.boot.trace;

import lombok.Data;

/**
 * 跟踪指标
 *
 * @author sh
 * @date 2018/12/20
 */
@Data
public class BaseContext {
    protected final String traceId;
    protected final String rpcId;
    protected String traceName = "";
    protected String serviceName = "";
    protected String methodName = "";
    protected String resultCode = "";
    protected String callBackMsg = "";
    protected long logTime;

    public BaseContext(String traceId, String rpcId) {
        this.traceId = traceId;
        this.rpcId = rpcId;
    }
}
