package com.springboot.isa.yt.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.isa.yt.model.Comment;

public interface CommentService {
	Page<Comment> findByVideoUploadId(Long id, Pageable pageable);
	Comment save(Comment comment);
	
}
