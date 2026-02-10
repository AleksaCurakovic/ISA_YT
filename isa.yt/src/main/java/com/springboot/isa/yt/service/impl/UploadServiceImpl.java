package com.springboot.isa.yt.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mp4parser.IsoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.isa.yt.dto.UploadRequestDTO;
import com.springboot.isa.yt.messaging.Producer;
import com.springboot.isa.yt.messaging.UploadEvent;
import com.springboot.isa.yt.model.VideoUpload;
import com.springboot.isa.yt.repository.UploadRepository;
import com.springboot.isa.yt.service.UploadService;

import jakarta.transaction.Transactional;



@Service
public class UploadServiceImpl implements UploadService {
	
	
	@Autowired
	private UploadRepository uploadRepository;
	
	private final Path root; 
	
	@Autowired
	private Producer producer;

    @Autowired
    public UploadServiceImpl(@Value("${app.storage.root:uploads}") String rootDir) {
        this.root = Paths.get(rootDir).toAbsolutePath().normalize();
    }

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
	            videoUpload.setDuration(getVideoDurationSeconds(uploadRequest.getVideo()));
	            
	            UploadEvent uploadEvent = new UploadEvent(videoUpload);
	            producer.sendToJson("jsonQueue", uploadEvent);
	            producer.sendToProtoBuf("protoQueue", uploadEvent);

	            return uploadRepository.save(videoUpload);

	        } catch (Exception ex) {
	            deleteByPublicUrl(thumbnailUrl);
	            deleteByPublicUrl(videoUrl);
	            throw new RuntimeException("Upload failed; rolled back files", ex);
	        }
	}
    
    private int getVideoDurationSeconds(MultipartFile videoFile) throws IOException {
    	File tempFile = File.createTempFile("video-upload-", ".mp4");
        videoFile.transferTo(tempFile);

        try (FileChannel fc = FileChannel.open(tempFile.toPath(), StandardOpenOption.READ);
             IsoFile isoFile = new IsoFile(fc)) {

            int duration =  (int) (isoFile.getMovieBox().getMovieHeaderBox().getDuration()
                    / isoFile.getMovieBox().getMovieHeaderBox().getTimescale());

            return duration;
        } finally {
            tempFile.delete(); 
        }
    }
 
    
    @Cacheable(value = "thumbnails", key = "#filename")
    @Override
    public byte[] getThumbnail(String filename) {
        try {
            return readThumbnailBytesFromDisk(filename);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
	
    private byte[] readThumbnailBytesFromDisk(String filename) throws IOException {
        String safe = StringUtils.cleanPath(filename);
        if (safe.contains("..") || safe.contains("/") || safe.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename");
        }

        Path file = root.resolve("thumbnails").resolve(safe).normalize();
        Path thumbsDir = root.resolve("thumbnails").normalize();
        if (!file.startsWith(thumbsDir)) {
            throw new IllegalArgumentException("Invalid path");
        }

        return Files.readAllBytes(file);
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

	@Override
	public VideoUpload findById(Long id) {
		return uploadRepository.findById(id).orElseGet(null);
	}

	@Override
	public List<VideoUpload> findAllByAuthor(String author) {
		return uploadRepository.findAllByAuthor(author);
	}

	@Override
	@Transactional
	public VideoUpload incremenetViewCount(Long id) {
		VideoUpload vu = uploadRepository.findByIdForUpdate(id);
		vu.setViews(vu.getViews() + 1);
		return uploadRepository.save(vu);
	}

}
