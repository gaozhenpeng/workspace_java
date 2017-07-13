package com.at.zipkin.brave.parentid;

import brave.internal.HexCodec;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import org.apache.log4j.MDC;

/**
 * <p>Adds {@linkplain MDC} properties "traceId", "spanId", and "<strong>parentId</strong>" when a
 * {@link brave.Tracer#currentSpan() span is current}. These can be used in log correlation.
 * </p>
 * <p>see also {@link brave.context.log4j12.MDCCurrentTraceContext}
 * </p>
 */
public final class Log4j12MDCCurrentTraceContext extends CurrentTraceContext {
  public static Log4j12MDCCurrentTraceContext create() {
    return new Log4j12MDCCurrentTraceContext(new Default());
  }

  public static Log4j12MDCCurrentTraceContext create(CurrentTraceContext delegate) {
    return new Log4j12MDCCurrentTraceContext(delegate);
  }

  final CurrentTraceContext delegate;

  Log4j12MDCCurrentTraceContext(CurrentTraceContext delegate) {
    if (delegate == null) {
      throw new NullPointerException("delegate == null");
    }
    this.delegate = delegate;
  }

  @Override
  public TraceContext get() {
    return delegate.get();
  }

  @Override
  public Scope newScope(TraceContext currentSpan) {
    final Object previousTraceId = MDC.get("traceId");
    final Object previousParentId = MDC.get("parentId");
    final Object previousSpanId = MDC.get("spanId");

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
      if (previousTraceId != null) {
        MDC.put("traceId", previousTraceId);
      } else {
        MDC.remove("traceId");
      }

      if (previousParentId != null) {
        MDC.put("parentId", previousParentId);
      } else {
        MDC.remove("parentId");
      }
      
      if (previousSpanId != null) {
        MDC.put("spanId", previousSpanId);
      } else {
        MDC.remove("spanId");
      }
    };
  }
}
