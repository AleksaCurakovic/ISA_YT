package com.springboot.isa.consumer.messaging;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.protobuf.InvalidProtocolBufferException;

import tools.jackson.databind.ObjectMapper;

@Component
public class Consumer {

	private static final Logger log = LoggerFactory.getLogger(Consumer.class);
	
	private ArrayList<Long> jsonDeserilizationTime = new ArrayList<Long>();
	
	private ArrayList<Long> protoDeserilizationTime = new ArrayList<Long>();
	
	private int jsonCount = 0;
	
	private int protoCount = 0;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@RabbitListener(queues="${myqueue1}")
    public void handleJsonQueue(Message message){
        log.info("Json message recieved");
        jsonCount += 1;
        byte[] bytes = message.getBody();
        long start = System.nanoTime();
        objectMapper.readValue(bytes, UploadEvent.class);
        long end = System.nanoTime();
        long deserDiff = end - start;
        jsonDeserilizationTime.add(deserDiff);
        if (jsonCount == 50) {
        	double averageTime = jsonDeserilizationTime.stream().mapToLong(Long::longValue).average().orElse(0);
        	log.info("Json average deserilization time " + averageTime/1000000 + " ms");
        }
    }
	
	@RabbitListener(queues="${myqueue2}")
    public void handleProtoQueue(Message message){
        log.info("Proto message recieved");
        byte[] bytes = message.getBody();
	    try {
	    	long start = System.nanoTime();
	    	UploadEventProto.UploadEventMsg event = UploadEventProto.UploadEventMsg.parseFrom(bytes);
			long end = System.nanoTime();
			long deserDiff = end - start;
			protoDeserilizationTime.add(deserDiff);
			protoCount += 1 ;
			if (protoCount == 50) {
	        	double averageTime = protoDeserilizationTime.stream().mapToLong(Long::longValue).average().orElse(0);
	        	log.info("Proto average deserilization time " + averageTime/1000000 + " ms");
	        }
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
