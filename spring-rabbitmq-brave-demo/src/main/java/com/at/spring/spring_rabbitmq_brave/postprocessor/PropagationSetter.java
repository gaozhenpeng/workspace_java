package com.at.spring.spring_rabbitmq_brave.postprocessor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import brave.propagation.Propagation;

@Component
public class PropagationSetter implements Propagation.Setter<Map<String, String>, String> {
	private static final Logger logger = LoggerFactory.getLogger(PropagationGetter.class);

	@Override
	public void put(Map<String, String> carrier, String key, String value) {
		if(logger.isDebugEnabled()){
			logger.debug("carrier: "+carrier + "; key: " + key + "; value: " + value + ";");
		}
		if(carrier == null || key == null || value == null){
			return;
		}
		carrier.put(key, value);
	}
}
