package com.springboot.isa.yt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.support.TransactionTemplate;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.service.UploadService;

@SpringBootTest
public class UploadServiceTest {
	@Autowired
    private UploadService uploadService;

    @Autowired
    private PlatformTransactionManager transactionManager;

     
	@Test
	void test_save_rollback_error() {
		

		MockMultipartFile thumbnail = new MockMultipartFile(
		        "thumbnail",
	            "thumb.jpg",
	            "image/jpeg",
	            "dummy thumbnail content".getBytes()
				);
	
		MockMultipartFile video = new MockMultipartFile(
			     "video",
			     "video.mp4",
			     "video/mp4",
			     new byte[1024 * 1024] // 1 MB dummy video content
				);
	
		 UploadRequestDTO request = new UploadRequestDTO();
		 request.setAuthor("Aleksa");
		 request.setTitle("Test Video");
		 request.setDescription("Test description");
		 request.setGeoLocation("Belgrade");
		 request.setThumbnail(thumbnail);
		 request.setVideo(video);
		
		 TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
	
	    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
	        txTemplate.execute(status -> {
	        	uploadService.save(request);
	            return null;
	        });
	    });
	    
	        
     }
}
