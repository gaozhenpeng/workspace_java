package com.at.spring.spring_rabbitmq_brave.postprocessor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import brave.propagation.Propagation;

@Component
public class PropagationGetter implements Propagation.Getter<Map<String, String>, String> {
	private static final Logger logger = LoggerFactory.getLogger(PropagationGetter.class);
	@Override
	public String get(Map<String, String> carrier, String key) {
		if(logger.isDebugEnabled()){
			logger.debug("carrier: " + carrier + "; key: " + key);
		}
		if(carrier == null || key == null){
			return null;
		}
		Object result = carrier.get(key);
		if(result == null){
			return null;
		}
		if(!String.class.isAssignableFrom(result.getClass())){
			return result.toString();
		}
		
		return (String)result;
	}
}
