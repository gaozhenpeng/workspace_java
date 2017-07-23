package com.at.spring.spring_rabbitmq_brave.brave.logproperty;

import brave.internal.HexCodec;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import org.slf4j.MDC;

/**
 * <p>Adds {@linkplain MDC} properties "traceId", "spanId", and "<strong>parentId</strong>" when a
 * {@link brave.Tracer#currentSpan() span is current}. These can be used in log correlation.
 * </p>
 * <p>see also {@link brave.context.slf4j.MDCCurrentTraceContext}
 * </p>
 */
public final class Slf4jMDCCurrentTraceContext extends CurrentTraceContext {
  public static Slf4jMDCCurrentTraceContext create() {
    return new Slf4jMDCCurrentTraceContext(new CurrentTraceContext.Default());
  }

  public static Slf4jMDCCurrentTraceContext create(CurrentTraceContext delegate) {
    return new Slf4jMDCCurrentTraceContext(delegate);
  }

  final CurrentTraceContext delegate;

  Slf4jMDCCurrentTraceContext(CurrentTraceContext delegate) {
    if (delegate == null) throw new NullPointerException("delegate == null");
    this.delegate = delegate;
  }

  @Override public TraceContext get() {
    return delegate.get();
  }

  @Override public Scope newScope(TraceContext currentSpan) {
    final String previousTraceId = MDC.get("traceId");
    final String previousParentId = MDC.get("parentId");
    final String previousSpanId = MDC.get("spanId");

    if (currentSpan != null) {
      MDC.put("traceId", currentSpan.traceIdString());
      Long parentId = currentSpan.parentId();
      MDC.put("parentId", parentId != null ? HexCodec.toLowerHex(parentId) : "");
      MDC.put("spanId", HexCodec.toLowerHex(currentSpan.spanId()));
    } else {
      MDC.remove("traceId");
      MDC.remove("parentId");
      MDC.remove("spanId");
    }

    Scope scope = delegate.newScope(currentSpan);
    return () -> {
      scope.close();
      MDC.put("traceId", previousTraceId);
      MDC.put("parentId", previousParentId);
      MDC.put("spanId", previousSpanId);
    };
  }
}
