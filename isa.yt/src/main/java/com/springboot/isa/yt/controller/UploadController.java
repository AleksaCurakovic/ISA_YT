package com.springboot.isa.yt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.model.VideoUpload;
import com.springboot.isa.yt.service.UploadService;

@RestController
public class UploadController {

	@Autowired
	private UploadService uploadService;
	
    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoUpload> uploadVideo(@ModelAttribute UploadRequestDTO uploadRequest){
		return ResponseEntity.ok(uploadService.save(uploadRequest));
	}
}
