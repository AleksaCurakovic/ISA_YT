package com.springboot.isa.yt.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.isa.yt.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long>{
	Page<Comment> findByVideoUploadId(Long id, Pageable pageable);
}
