package com.springboot.isa.yt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.springboot.isa.yt.model.VideoUpload;

import jakarta.persistence.LockModeType;

public interface UploadRepository extends JpaRepository<VideoUpload, Long>{
	VideoUpload findByAuthor(String author);
	List<VideoUpload> findAll();
	List<VideoUpload> findAllByAuthor(String author);
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT vu FROM VideoUpload vu WHERE vu.id = :id")
    VideoUpload findByIdForUpdate(Long id);
}
