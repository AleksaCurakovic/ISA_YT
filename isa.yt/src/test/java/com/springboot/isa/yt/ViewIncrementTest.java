package com.springboot.isa.yt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.springboot.isa.yt.controller.AuthenticationContoller;
import com.springboot.isa.yt.dto.JwtAuthenticationRequestDTO;
import com.springboot.isa.yt.model.VideoUpload;
import com.springboot.isa.yt.repository.UploadRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest
@AutoConfigureMockMvc
public class ViewIncrementTest {
	 	@Autowired
	    private MockMvc mockMvc;
	 	
	 	@Autowired
	 	private UploadRepository uploadRepository;
	 	
	 	@Autowired
	 	private AuthenticationContoller authController;


	    @Test
	    void test_parallel_increment_view_count() throws Exception {
	    	
	    	JwtAuthenticationRequestDTO authRequest = new JwtAuthenticationRequestDTO("aleksa02", "123456");
	    	HttpServletRequest request = mock(HttpServletRequest.class);
	    	HttpServletResponse response = mock(HttpServletResponse.class);
	    	var authResponse = authController.createAuthenticationToken(authRequest, response, request);
	    	
	        int threadCount = 10;

	        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
	        CountDownLatch latch = new CountDownLatch(threadCount);

	        for (int i = 0; i < threadCount; i++) {
	            executor.submit(() -> {
	                try {
	                	mockMvc.perform(
	        	                MockMvcRequestBuilders.request(HttpMethod.PATCH, "/incrementView/1")
	        	                .header("Authorization", "Bearer " + authResponse.getBody().getAccessToken())
	        	                .contentType(MediaType.APPLICATION_JSON)).andDo(print());
	                } catch (Exception e) {
	                    e.printStackTrace();
	                } finally {
	                    latch.countDown();
	                }
	            });
	        }

	        latch.await();
	        executor.shutdown();
	        
	        VideoUpload video = uploadRepository.findById(1L)
	                .orElseThrow(() -> new IllegalStateException("Video not found"));

	        // Assert that 10 views were incremented
	        assertEquals(10, video.getViews(), "View count should be 10 after 10 parallel requests");
	    }
}
