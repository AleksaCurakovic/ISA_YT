package com.springboot.isa.yt.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class MQConfiguratio {
	
	@Value("${myqueue1}")
	String queue1;
	 
	@Bean
    Queue queue1() {
		return new Queue(queue1, true);
    }
	
	@Value("${myqueue2}")
	String queue2;
	 
	@Bean
    Queue queue2() {
		return new Queue(queue2, true);
    }
	
	@Bean
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory("localhost");
	}

}
