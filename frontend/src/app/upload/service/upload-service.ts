import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VideoUpload } from '../../model/videoUpload';
import { VideoPreview } from '../../model/videoPreview';
import { HttpEvent } from '@angular/common/http';


@Injectable({
  providedIn: 'root',
})
export class UploadService  {
  private readonly API_URL = 'http://localhost:8080';
  constructor(private http: HttpClient) {}

  uploadVideo(formData: FormData): Observable<HttpEvent<VideoUpload>> {
    return this.http.post<VideoUpload>(`${this.API_URL}/upload`, formData, {
      reportProgress: true,
      observe: 'events',
    })
  }

  getAllUploads(): Observable<VideoPreview[]> {
    return this.http.get<VideoPreview[]>(`${this.API_URL}/getAllUploads`);
  }

  getUpload(id:number): Observable<VideoUpload> {
    return this.http.get<VideoUpload>(`${this.API_URL}/getUpload/${id}`);
  }

  getUserUploads(userName: string): Observable<VideoPreview[]> {
    return this.http.get<VideoPreview[]>(`${this.API_URL}/getUserUploads/${userName}`);
  }

  incrementViewCount(videoId: number): Observable<void> {
    return this.http.patch<void>(`${this.API_URL}/incrementView/${videoId}`, {});
  }
}
