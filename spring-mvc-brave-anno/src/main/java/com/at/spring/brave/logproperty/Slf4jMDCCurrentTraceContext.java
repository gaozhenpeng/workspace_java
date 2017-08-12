package com.at.spring.brave.logproperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import brave.internal.HexCodec;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;

/**
 * <p>
 * Adds {@linkplain MDC} properties "traceId", "spanId", and
 * "<strong>parentId</strong>" when a {@link brave.Tracer#currentSpan() span is
 * current}. These can be used in log correlation.
 * </p>
 * <p>
 * see also {@link brave.context.slf4j.MDCCurrentTraceContext}
 * </p>
 */
public final class Slf4jMDCCurrentTraceContext extends CurrentTraceContext {
	private static final Logger logger = LoggerFactory.getLogger(Slf4jMDCCurrentTraceContext.class);
	
	public static Slf4jMDCCurrentTraceContext create() {
		logger.trace("start create()");
		return new Slf4jMDCCurrentTraceContext(new CurrentTraceContext.Default());
	}

	public static Slf4jMDCCurrentTraceContext create(CurrentTraceContext delegate) {
		logger.trace("start create(CurrentTraceContext)");
		return new Slf4jMDCCurrentTraceContext(delegate);
	}

	final CurrentTraceContext delegate;

	Slf4jMDCCurrentTraceContext(CurrentTraceContext delegate) {
		logger.trace("start Slf4jMDCCurrentTraceContext(CurrentTraceContext)");
		if (delegate == null)
			throw new NullPointerException("delegate == null");
		this.delegate = delegate;
	}

	@Override
	public TraceContext get() {
		logger.trace("start get()");
		return delegate.get();
	}

	@Override
	public Scope newScope(TraceContext currentSpan) {
		logger.trace("start newScope(TraceContext)");
		
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
 
		final Scope scope = delegate.newScope(currentSpan);
		return new Scope() {
			@Override
			public void close(){
				scope.close();
				MDC.put("traceId", previousTraceId);
				MDC.put("parentId", previousParentId);
				MDC.put("spanId", previousSpanId);
			}
		};
	}
}
