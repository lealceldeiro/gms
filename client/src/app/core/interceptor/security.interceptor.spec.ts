import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs/index';

import { SecurityInterceptor } from './security.interceptor';
import { SessionService } from '../session/session.service';

describe('SecurityInterceptor', () => {
  const header = 'header';
  const tokenType = 'tokenType';
  const accessToken = 'accessToken';
  const liBS = new BehaviorSubject<boolean>(true);
  const sessionServiceStub = {
    isLoggedIn: function(): Observable<Boolean> {
      return liBS.asObservable();
    },
    getHeader: function(): Observable<String>  {
      return of(header);
    },
    getTokenType: function(): Observable<String>  {
      return of(tokenType);
    },
    getAccessToken: function(): Observable<String>  {
      return of(accessToken);
    }
  };
  let httpCtrl: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [
        SecurityInterceptor,
        { provide: HTTP_INTERCEPTORS, useClass: SecurityInterceptor, multi: true },
        { provide: SessionService, useValue: sessionServiceStub }
      ]
    });
    httpCtrl = TestBed.get(HttpTestingController);
    httpClient = TestBed.get(HttpClient);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpCtrl.verify();
  });

  it('should be created', inject([SecurityInterceptor], (service: SecurityInterceptor) => {
    expect(service).toBeTruthy();
  }));

  it('should add the authorization header when the user is logged in', () => {
    liBS.next(true);
    httpClient.get('test').subscribe((response: any) => {
      expect(response).toBeTruthy();
    });
    const testRequest = httpCtrl.expectOne(request =>
      request.headers.has(header) && request.headers.get(header) === tokenType + ' ' + accessToken
    );
    testRequest.flush({ data: 'test' });
    httpCtrl.verify();
  });

  it('should not add the authorization header when the user is not logged in', () => {
    liBS.next(false);
    httpClient.get('test').subscribe((response: any) => {
      expect(response).toBeTruthy();
    });
    const testRequest = httpCtrl.expectOne(request =>
      !request.headers.has(header) || (request.headers.get(header) !== tokenType + ' ' + accessToken)
    );
    testRequest.flush({ data: 'test' });
  });
});
