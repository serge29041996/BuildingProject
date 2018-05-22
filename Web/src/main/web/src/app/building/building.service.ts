import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Building } from "../building";
import { HttpHeaders } from "@angular/common/http";
import {catchError} from "rxjs/operators/catchError";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorObservable} from "rxjs/observable/ErrorObservable";

@Injectable()
export class BuildingService {
  constructor(private http: HttpClient) {
  }

  getAll(): Observable<any> {
    return this.http.get('//localhost:8080/buildings');
  }

  getBuilding(id: number){
    return this.http.get('//localhost:8080/building/' + id)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteBuilding(building:Building){
    return this.http.delete('//localhost:8080/building/' + building.id);
  }

  addBuilding(building:Building){
    let body = JSON.stringify(building);
    let header = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post('//localhost:8080/building',body,{headers: header})
      .pipe(
        catchError(this.handleError)
      )
  }

  updateBuilding(building:Building){
    let body = JSON.stringify(building);
    let header = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put('//localhost:8080/building/'+building.id,body,{headers: header})
      .pipe(
        catchError(this.handleError)
      )
  }

  private handleError(error: HttpErrorResponse) {
    return new ErrorObservable(error.error);
  }
}
