package com.springboot.isa.yt.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller	
public class WebSocketController {
	 @Autowired
	 private SimpMessagingTemplate simpMessagingTemplate;
	 
	 private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
	 
	 @MessageMapping("/send")
	 public void broadcastNotification(String message) {
	    Map<String, String> messageConverted = parseMessage(message);
	    
	    log.info(messageConverted.toString());
	
        if (messageConverted.containsKey("guest") && messageConverted.containsKey("host") && messageConverted.containsKey("joined")) {
        	String appendMsg;
        	if (!messageConverted.get("joined").equals("true"))
        		appendMsg = "has left your watch party";
        	else
        		appendMsg = "has joined your watch party";
        	
            this.simpMessagingTemplate.convertAndSend("/watchparty/" + messageConverted.get("host"),
                    messageConverted.get("guest") + appendMsg);
        } else {
            this.simpMessagingTemplate.convertAndSend("/watchparty/"+ messageConverted.get("host") + "/guests",
            		messageConverted.get("videoId"));
        }
	
	}
	 
 	@SuppressWarnings("unchecked")
    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> retVal;

        try {
            retVal = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            retVal = null;
        }
        return retVal;
    }

}
