import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient } from '@angular/common/http';
import { FormatWidth } from '@angular/common';


@Injectable({
  providedIn: 'root'
})
export class FileOperationServiceService {

  private baseUrlStates = "http://localhost:8080/file";

  constructor(private httpClient: HttpClient) {

  }

  public sendSerachText(searchText: FormData): Observable<any>{
    return this.httpClient
                  .post(`${this.baseUrlStates}/search`,searchText,{responseType:"blob"});
  }

  public sendLink(fileLink: FormData): Observable<any>{
      return this.httpClient
                  .post(`${this.baseUrlStates}/link`,fileLink,{responseType:"blob"});
  }

  public sendFile(filetoUpload: FormData) : Observable<any>{
    return this.httpClient
                  .post(`${this.baseUrlStates}/upload`,filetoUpload,{responseType:"blob"});
  }
}
