package com.saucesubfresh.starter.trace.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author lijunping on 2022/7/22
 */
public class TraceContext {

    private static final TransmittableThreadLocal<String> TRACE_ID_CONTEXT = new TransmittableThreadLocal<>();

    public static void putTraceId(String traceId) {
        TRACE_ID_CONTEXT.set(traceId);
    }

    public static String getTraceId() {
        return TRACE_ID_CONTEXT.get();
    }

    public static void removeTraceId() {
        TRACE_ID_CONTEXT.remove();
    }
}
