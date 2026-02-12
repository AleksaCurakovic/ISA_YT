package com.springboot.isa.yt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.model.User;
import com.springboot.isa.yt.service.CommentService;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;

@RestController
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private RateLimiterRegistry rateLimiterRegistry;
	
	@GetMapping("/comments/{videoId}")
	public ResponseEntity<Page<Comment>> getComments(
	        @PathVariable Long videoId,
	        @RequestParam int page,
	        @RequestParam int size) {
	    
	    PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
	    
	    return ResponseEntity.ok(commentService.findByVideoUploadId(videoId, pageable));
	}
	
	@PostMapping("/comment")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var config = rateLimiterRegistry.rateLimiter("commentLimit").getRateLimiterConfig();
		User user = (User) authentication.getPrincipal();
		RateLimiter perUserLimiter = rateLimiterRegistry.rateLimiter("comment:" + user.getUsername(), config);
		try {
		    RateLimiter.waitForPermission(perUserLimiter); 
		    return ResponseEntity.ok(commentService.save(comment));
		} catch (RequestNotPermitted ex) {
		    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
		}
	}
}
