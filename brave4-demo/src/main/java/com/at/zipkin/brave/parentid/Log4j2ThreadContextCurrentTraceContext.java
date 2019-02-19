package com.at.zipkin.brave.parentid;

import org.apache.logging.log4j.ThreadContext;

import brave.internal.HexCodec;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;

/**
 * <p>Adds {@linkplain ThreadContext} properties "traceId", "spanId", and "<strong>parentId</strong>" when a
 * {@link brave.Tracer#currentSpan() span is current}. These can be used in log correlation.
 * </p>
 * <p>see also {@link brave.context.log4j2.ThreadContextCurrentTraceContext}
 * </p>
 */
public final class Log4j2ThreadContextCurrentTraceContext extends CurrentTraceContext {
  public static Log4j2ThreadContextCurrentTraceContext create() {
    return new Log4j2ThreadContextCurrentTraceContext(new CurrentTraceContext.Default());
  }

  public static Log4j2ThreadContextCurrentTraceContext create(CurrentTraceContext delegate) {
    return new Log4j2ThreadContextCurrentTraceContext(delegate);
  }

  final CurrentTraceContext delegate;

  Log4j2ThreadContextCurrentTraceContext(CurrentTraceContext delegate) {
    if (delegate == null) throw new NullPointerException("delegate == null");
    this.delegate = delegate;
  }

  @Override public TraceContext get() {
    return delegate.get();
  }

  @Override public Scope newScope(TraceContext currentSpan) {
    final String previousTraceId = ThreadContext.get("traceId");
    final String previousParentId = ThreadContext.get("parentId");
    final String previousSpanId = ThreadContext.get("spanId");

    if (currentSpan != null) {
      ThreadContext.put("traceId", currentSpan.traceIdString());
      Long parentId = currentSpan.parentId();
      ThreadContext.put("parentId", parentId != null ? HexCodec.toLowerHex(parentId) : "");
      ThreadContext.put("spanId", HexCodec.toLowerHex(currentSpan.spanId()));
    } else {
      ThreadContext.remove("traceId");
      ThreadContext.remove("spanId");
    }

    Scope scope = delegate.newScope(currentSpan);
    return () -> {
      scope.close();
      ThreadContext.put("traceId", previousTraceId);
      ThreadContext.put("parentId", previousParentId);
      ThreadContext.put("spanId", previousSpanId);
    };
  }
}
