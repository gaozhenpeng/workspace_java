package com.at.spring.spring_rabbitmq.configuration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.at.spring.spring_rabbitmq.listener.DirectMessageListener;
import com.at.spring.spring_rabbitmq.listener.HeaderMessageListener;
import com.at.spring.spring_rabbitmq.postprocessor.AfterReceivePostProcessor;
import com.at.spring.spring_rabbitmq.postprocessor.BeforePublishPostProcessor;

@Configuration
public class RabbitmqConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(RabbitmqConfiguration.class);
	
	@Autowired
	private DirectMessageListener directMessageListener;
	@Autowired
	private HeaderMessageListener headerMessageListener;
	@Autowired
	private BeforePublishPostProcessor beforePublishPostProcessor;
	@Autowired
	private AfterReceivePostProcessor afterReceivePostProcessor;

	/**
	 * add user and vhost
	 *   rabbitmqctl add_user test test123
	 *   rabbitmqctl add_vhost /mq_test
	 *   rabbitmqctl set_permissions -p /mq_test test ".*" ".*" ".*"
	 *   rabbitmqctl set_user_tags test management
	 * 
	 * @return
	 */
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setHost("localhost");
		ccf.setPort(5672);
		ccf.setUsername("test");
		ccf.setPassword("test123");
		ccf.setVirtualHost("/mq_test");
		return ccf;
	}

	/**
	 * auto declare queues, exchanges, and bindings
	 */
	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory());
		
		rabbitTemplate.setBeforePublishPostProcessors(beforePublishPostProcessor);
		rabbitTemplate.setAfterReceivePostProcessors(afterReceivePostProcessor);
		return rabbitTemplate;
	}

	@Bean
	public Queue myDirectQueue() {
		return new Queue("mydirectqueue");
	}
	@Bean
	public Queue myHeadersQueue() {
		return new Queue("myheadersqueue");
	}
	@Bean
	public Queue myDirectQueueForListener() {
		return new Queue("mydirectqueueforlistener");
	}
	@Bean
	public Queue myHeaderQueueForListener() {
		return new Queue("myheaderqueueforlistener");
	}
	@Bean
	public HeadersExchange headersExchange() {
		HeadersExchange headersExchange = new HeadersExchange("myheadersexchange", true, true, null);
		return headersExchange;
	}

	@Bean
	public DirectExchange directExchange() {
		DirectExchange directExchange = new DirectExchange("mydirectexchange", true, true, null);
		return directExchange;
	}

	@Bean
	public Binding directBinding() {
//		return BindingBuilder.bind(myDirectQueue()).to(directExchange()).withQueueName(); // routingkey: mydirectqueue
		return BindingBuilder.bind(myDirectQueue()).to(directExchange()).with(myDirectQueue().getName()+ "key"); // routingkey: mydirectqueuekey
	}

	@Bean
	public Binding directBindingForListener() {
//		return BindingBuilder.bind(myDirectQueueForListener()).to(directExchange()).withQueueName(); // routingkey: mydirectqueueforlistener
		return BindingBuilder.bind(myDirectQueueForListener()).to(directExchange()).with(myDirectQueueForListener().getName()+ "key"); // mydirectqueueforlistenerkey
	}

	@Bean
	public Binding headerBinding() {
		Map<String, Object> headers = new HashMap<String, Object>() {
			private static final long serialVersionUID = 4836615343679142680L;

			{
				put("name", "test");
				put("sex", "male");
			}
		};
//		return BindingBuilder.bind(myHeadersQueue()).to(headersExchange()).whereAny(headers).match();
		return BindingBuilder.bind(myHeadersQueue()).to(headersExchange()).whereAll(headers).match();
	}


	@Bean
	public Binding headerBindingForListener() {
		Map<String, Object> headers = new HashMap<String, Object>() {
			private static final long serialVersionUID = 8302964877816552241L;

			{
				put("name", "test");
				put("sex", "female");
			}
		};
		return BindingBuilder.bind(myHeaderQueueForListener()).to(headersExchange()).whereAll(headers).match();
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainerForDirect() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueues(myDirectQueueForListener());
		container.setAfterReceivePostProcessors(afterReceivePostProcessor);
		container.setMessageListener(directMessageListener);
		return container;
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainerForHeader() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setAfterReceivePostProcessors(afterReceivePostProcessor);
		container.setQueues(myHeaderQueueForListener());
		container.setMessageListener(headerMessageListener);
		return container;
	}

}
