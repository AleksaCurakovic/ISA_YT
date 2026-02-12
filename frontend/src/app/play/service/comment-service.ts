import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../model/comment'
import { Pagable } from '../../model/pagable'

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private readonly API_URL = 'http://localhost:8080';
  constructor(private http: HttpClient){}

  getVideoComments(id: number, page: number, size: number): Observable<Pagable<Comment>>{
    return this.http.get<Pagable<Comment>>(`${this.API_URL}/comments/${id}?page=${page}&size=${size}`);
  }

  commentOnVideo(comment: Comment): Observable<Comment>{
    return this.http.post<Comment>(`${this.API_URL}/comment`, comment);
  }
}
