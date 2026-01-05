import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoUpload } from '../../model/videoUpload';
import { CommentService } from '../service/comment-service';
import { UploadService } from '../../upload/service/upload-service';
import { ActivatedRoute } from '@angular/router';
import { Comment } from '../../model/comment';

@Component({
  selector: 'app-video-play',
  imports: [CommonModule],
  templateUrl: './video-play.html',
  styleUrl: './video-play.css',
})
export class VideoPlay implements OnInit {
  video!: VideoUpload

  suggestedVideos: VideoUpload[] = [];
  comments: Comment[] = []

  constructor(private commentService: CommentService, private uploadService: UploadService,
              private route: ActivatedRoute
  ){}

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
    const videoId = params['videoId'];
    this.uploadService.getAllUploads().subscribe(videos => {
      this.suggestedVideos = videos;
    })
    if (videoId) {
      this.uploadService.getUpload(videoId).subscribe(video => {
        this.video = video;
      }, error => {
        console.error('Failed to load video', error);
      });
      this.commentService.getVideoComments(videoId).subscribe(comments => {
        this.comments = comments;
      }, error => {
        console.error('Failed to load video', error);
      });
    }
  });
  }
}
