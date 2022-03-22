package com.bridgelabz.fundoouser.rabbitmqconfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
	 
	public static final String QUEUE = "email_queue";
	public static final String EXCHANGE = "email_exchange";
	public static final String ROUTING_KEY = "email_routingKey";

	//queue name is imp as it connects producer and listener
	@Bean
	Queue queue() {
		return new Queue(QUEUE);
	}
	
	//instant exchanges
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(EXCHANGE);
	}
	
	//binding queue to exchanges
	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}
	
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
