package com.springboot.isa.yt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.isa.yt.model.Comment;
import com.springboot.isa.yt.repository.CommentRepository;
import com.springboot.isa.yt.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	@Override
	@Cacheable(value = "comments", 
    	key = "{#id, #pageable.pageNumber, #pageable.pageSize}")
	public Page<Comment> findByVideoUploadId(Long id, Pageable pageable) {
		return commentRepository.findByVideoUploadId(id, pageable);
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

}
