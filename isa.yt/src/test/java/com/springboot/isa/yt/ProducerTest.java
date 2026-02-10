package com.springboot.isa.yt;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.isa.yt.messaging.Producer;
import com.springboot.isa.yt.messaging.UploadEvent;
import com.springboot.isa.yt.model.VideoUpload;

@SpringBootTest
public class ProducerTest {
	
	@Autowired
	private Producer producer;
	
	@Test
	void test_proto_vs_json() {
		
		for(int i = 1; i < 51; i++) {
			VideoUpload videoUpload = new VideoUpload(
			        (long) i,
			        "Video " + i,
			        "Author " + i,
			        "Description " + i,
			        "tag1,tag2",
			        "https://thumb/" + i,
			        "https://video/" + i,
			        new Date(),
			        "Serbia",
			        1000 + i,
			        100 + i,
			        300 + i
			);
			UploadEvent uploadEvent = new UploadEvent(videoUpload);
			producer.sendToProtoBuf("protoQueue", uploadEvent);
			producer.sendToJson("jsonQueue", uploadEvent);
			
		}
	}

}
