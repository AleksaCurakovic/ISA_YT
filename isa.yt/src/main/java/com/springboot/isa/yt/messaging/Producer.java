package com.springboot.isa.yt.messaging;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Producer {
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static final Logger log = LoggerFactory.getLogger(Producer.class);
	
	private ArrayList<Long> jsonSerilizationTimes =  new ArrayList<>();
	
	private ArrayList<Long> protoSerilizationTimes = new ArrayList<>();
	
	private int jsonCount = 0;
	
	private int protoCount = 0;
		
	public void sendToJson(String routingKey, UploadEvent message) {
		try {
			long start = System.nanoTime();
			byte[] bytes = objectMapper.writeValueAsBytes(message);
			long end = System.nanoTime();
			long serDiff = end - start;
			
			jsonSerilizationTimes.add(serDiff);
			jsonCount += 1;
			log.info("Sending json message with size " + bytes.length + " bytes");
			rabbitTemplate.send(routingKey, new Message(bytes, new MessageProperties()));
			if (jsonCount == 50) {
				double averageTime = jsonSerilizationTimes.stream().mapToLong(Long::longValue).average().orElse(0);
				log.info("Average json serilization time " + averageTime/100000 + "ms");
			}
         
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void sendToProtoBuf(String routingKey, UploadEvent message) {
		 try {
             UploadEventProto.UploadEventMsg protoMsg = UploadEventProto.UploadEventMsg.newBuilder()
                     .setTitle(message.getTitle())
                     .setAuthor(message.getAuthor())
                     .setDescription(message.getDescription())
                     .setTags(message.getTags())
                     .setCreatedAt(message.getCreatedAt().getTime())
                     .setViews(message.getViews())
                     .build();

             long start = System.nanoTime();
             byte[] protoBytes = protoMsg.toByteArray();
             long end = System.nanoTime();
             
             long serDiff = end - start;
             protoSerilizationTimes.add(serDiff);
             protoCount += 1;
             log.info("Sending Protobuf message with size " + protoBytes.length + " bytes");
             rabbitTemplate.send(routingKey, new Message(protoBytes, new MessageProperties()));
             
             if (protoCount == 50) {
 				double averageTime = protoSerilizationTimes.stream().mapToLong(Long::longValue).average().orElse(0);
 				log.info("Average protobuf serilization time " + averageTime/1000000 + " ms");
 			}

         } catch (Exception e) { e.printStackTrace(); }
		
    }


}
