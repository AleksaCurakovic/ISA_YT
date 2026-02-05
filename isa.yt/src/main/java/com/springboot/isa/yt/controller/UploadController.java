package com.springboot.isa.yt.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.dto.VideoPreviewDTO;
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
    
    @GetMapping("/uploads/thumbnails/{filename}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable String filename){
    	byte[] bytes = uploadService.getThumbnail(filename);
    	return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) 
                .body(bytes);
    }
    
    @GetMapping("/getAllUploads")
    public ResponseEntity<List<VideoPreviewDTO>> getAllUploads(){
    	List<VideoPreviewDTO> videoDTOs =
    	        uploadService.findAll()
    	            .stream()
    	            .map(VideoPreviewDTO::new)  
    	            .toList();                

    	return ResponseEntity.ok(videoDTOs);   	
    }
    
    @GetMapping("/getUpload/{uploadId}")
    public ResponseEntity<VideoUpload> getUpload(@PathVariable Long uploadId){
    	return ResponseEntity.ok(uploadService.findById(uploadId));
    }
    
    @GetMapping("/getUserUploads/{userName}")
    public ResponseEntity<List<VideoPreviewDTO>> getUserUploads(@PathVariable String userName){
    	List<VideoPreviewDTO> videoDTOs =
    	        uploadService.findAllByAuthor(userName)
    	            .stream()
    	            .map(VideoPreviewDTO::new)  
    	            .toList();                

    	return ResponseEntity.ok(videoDTOs);   	
    }
    
    @PatchMapping("/incrementView/{uploadId}")
    public ResponseEntity<VideoUpload> incrementViewCount(@PathVariable Long uploadId){
    	return  ResponseEntity.ok(uploadService.incremenetViewCount(uploadId));
    }
}
