package com.springboot.isa.yt.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.model.VideoUpload;
import com.springboot.isa.yt.repository.UploadRepository;
import com.springboot.isa.yt.service.UploadService;



@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired
	private UploadRepository uploadRepository;
	
	private final Path root;

    @Autowired
    public UploadServiceImpl(@Value("${app.storage.root:uploads}") String rootDir) {
        this.root = Paths.get(rootDir).toAbsolutePath().normalize();
    }

    @Transactional(timeout = 5)
	@Override
	public VideoUpload save(UploadRequestDTO uploadRequest) throws RuntimeException {
    	 String thumbnailUrl = "";
         String videoUrl = "";
		 try {
	            thumbnailUrl = saveFile(uploadRequest.getThumbnail(), "thumbnails");
	            videoUrl = saveFile(uploadRequest.getVideo(), "videos");

	            VideoUpload videoUpload = new VideoUpload();
	            videoUpload.setAuthor(uploadRequest.getAuthor());
	            videoUpload.setTitle(uploadRequest.getTitle());
	            videoUpload.setDescription(uploadRequest.getDescription());
	            videoUpload.setCreatedAt(new Date());
	            videoUpload.setTags(uploadRequest.getTags());
	            videoUpload.setGeoLocation(uploadRequest.getGeoLocation());
	            videoUpload.setThumbnailUrl(thumbnailUrl);
	            videoUpload.setVideoUrl(videoUrl);
	            
	            //Thread.sleep(6000); uncomment for transaction

	            return uploadRepository.save(videoUpload);

	        } catch (Exception ex) {
	            deleteByPublicUrl(thumbnailUrl);
	            deleteByPublicUrl(videoUrl);
	            throw new RuntimeException("Upload failed; rolled back files", ex);
	        }
	}
	
	 private String saveFile(MultipartFile file, String subDir) throws IOException {
	        if (file == null || file.isEmpty()) {
	            throw new IllegalArgumentException(subDir + " file is required");
	        }

	        Path dir = root.resolve(subDir).normalize();
	        Files.createDirectories(dir);

	        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
	        String ext = "";
	        int dot = original.lastIndexOf('.');
	        if (dot >= 0) ext = original.substring(dot);

	        String filename = UUID.randomUUID() + ext;
	        Path target = dir.resolve(filename).normalize();

	        // prevent ../ traversal
	        if (!target.startsWith(dir)) {
	            throw new IllegalArgumentException("Invalid file path");
	        }

	        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

	        return "/uploads/" + subDir + "/" + filename;
	    }
	 
	 private void deleteByPublicUrl(String publicUrl) {
		    if (publicUrl == null) return;

		    String relative = publicUrl.startsWith("/uploads/")
		            ? publicUrl.substring("/uploads/".length())
		            : publicUrl;

		    Path p = root.resolve(relative).normalize();
		    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
		}


	@Override
	public VideoUpload findByAuthor(String author) {
		return uploadRepository.findByAuthor(author);
	}

	@Override
	public List<VideoUpload> findAll() {
		return uploadRepository.findAll();
	}

}
