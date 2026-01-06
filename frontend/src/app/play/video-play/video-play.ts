import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VideoUpload } from '../../model/videoUpload';
import { CommentService } from '../service/comment-service';
import { UploadService } from '../../upload/service/upload-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Comment } from '../../model/comment';
import { Auth } from '../../infrastructure/service/auth'

@Component({
  selector: 'app-video-play',
  imports: [CommonModule],
  templateUrl: './video-play.html',
  styleUrl: './video-play.css',
})
export class VideoPlay implements OnInit {
  video!: VideoUpload
  loggedInUsername: string | null = null;
  suggestedVideos: VideoUpload[] = []
  comments: Comment[] = []

  constructor(private commentService: CommentService, private uploadService: UploadService,
              private route: ActivatedRoute, private authService: Auth, private router: Router
  ){}

  ngOnInit(): void{
    this.authService.authState$.subscribe(username  => {
      this.loggedInUsername = username;
    })
    this.route.queryParams.subscribe(params => {
    const videoId = params['videoId'];
    this.uploadService.getAllUploads().subscribe(videos => {
      this.suggestedVideos = videos
    })
    if (videoId) {
      this.uploadService.getUpload(videoId).subscribe(video => {
        this.video = video
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

  likeVideo(): void {
    if (!this.loggedInUsername)
    {
      this.router.navigate(['/login'])
    }
    //Like logic
  }

  commentVideo(): void {
     if (!this.loggedInUsername)
    {
      this.router.navigate(['/login'])
    }
    //Comment logic
  }
}
