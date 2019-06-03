import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpErrorResponse } from '@angular/common/http';
import { NgxUiLoaderService } from 'ngx-ui-loader';

import { AllRequestsInterceptor } from './all-requests.interceptor';
import { HttpStatusCode } from '../response/http-status-code.enum';

describe('AllRequestsInterceptor', () => {

  const url = 'sample-url';
  const spy = { startLoader: () => { }, stopLoader: () => { } }
  const loaderServiceMock = { start: () => { spy.startLoader(); }, stopAll: () => { stopLoaderSpy(); } };

  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;
  let interceptor: AllRequestsInterceptor;
  let startLoaderSpy: jasmine.Spy;
  let stopLoaderSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AllRequestsInterceptor,
        { provide: HTTP_INTERCEPTORS, useClass: AllRequestsInterceptor, multi: true },
        { provide: NgxUiLoaderService, useValue: loaderServiceMock },
      ]
    });

    httpTestingController = TestBed.get(HttpTestingController);
    httpClient = TestBed.get(HttpClient);
    interceptor = TestBed.get(AllRequestsInterceptor);
    startLoaderSpy = spyOn(spy, 'startLoader');
    stopLoaderSpy = spyOn(spy, 'stopLoader');
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', () => {
    const service: AllRequestsInterceptor = TestBed.get(AllRequestsInterceptor);
    expect(service).toBeTruthy();
  });

  it('should call <loader_service>#start when the loader is not started and a request is performed', () => {
    httpClient.get(url).subscribe(() => expect(startLoaderSpy).toHaveBeenCalledTimes(1));
    httpTestingController.expectOne(url).flush({});
  });

  it('should NOT call <loader_service>#start when the loader is already started and a request is performed', () => {
    httpClient.get(url).subscribe(() => expect(startLoaderSpy).toHaveBeenCalledTimes(1));
    httpClient.get(url).subscribe(() => expect(startLoaderSpy).not.toHaveBeenCalledTimes(2));
    const req = httpTestingController.match(url);
    req[0].flush({});
    req[1].flush({});
  });

  it('should call <loader_service>#stopAll when the <loader_service>#start has been called previously and a successfull response arrived', () => {
    httpClient.get(url).subscribe(() => {
      expect(startLoaderSpy).toHaveBeenCalledTimes(1);
      expect(stopLoaderSpy).toHaveBeenCalledTimes(1);
    });
    httpTestingController.expectOne(url).flush({});
  });

  it('should call <loader_service>#stopAll when the <loader_service>#start has been called previously and an usuccessfull response arrived', () => {
    const error: HttpErrorResponse = new HttpErrorResponse({status: HttpStatusCode.UNAUTHORIZED, statusText: 'Server Error', url: url});
    httpClient.get(url).subscribe(() => {}, () => {
      // error
      expect(startLoaderSpy).toHaveBeenCalledTimes(1);
      expect(stopLoaderSpy).toHaveBeenCalledTimes(1);
    });
    httpTestingController.expectOne(url).flush({}, error);
  });
});