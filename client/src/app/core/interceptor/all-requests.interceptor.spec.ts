import { HttpClient, HttpErrorResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { NgxUiLoaderService } from 'ngx-ui-loader';

import { HttpStatusCode } from '../response/http-status-code.enum';
import { AllRequestsInterceptor } from './all-requests.interceptor';

describe('AllRequestsInterceptor', () => {
  const url = 'sample-url';
  let loaderServiceSpy: jasmine.SpyObj<NgxUiLoaderService>;
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    loaderServiceSpy = jasmine.createSpyObj('NgxUiLoaderService', ['start', 'stopAll']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AllRequestsInterceptor,
        { provide: HTTP_INTERCEPTORS, useClass: AllRequestsInterceptor, multi: true },
        { provide: NgxUiLoaderService, useValue: loaderServiceSpy },
      ]
    });

    httpTestingController = TestBed.get(HttpTestingController);
    httpClient = TestBed.get(HttpClient);
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
    httpClient.get(url).subscribe(() => expect(loaderServiceSpy.start).toHaveBeenCalledTimes(1));
    httpTestingController.expectOne(url).flush({});
  });

  it('should NOT call <loader_service>#start when the loader is already started and a request is performed', () => {
    httpClient.get(url).subscribe(() => expect(loaderServiceSpy.start).toHaveBeenCalledTimes(1));
    httpClient.get(url).subscribe(() => expect(loaderServiceSpy.start).not.toHaveBeenCalledTimes(2));
    const req = httpTestingController.match(url);
    req[0].flush({});
    req[1].flush({});
  });

  it('should call <loader_service>#stopAll when the <loader_service>#start has been called previously and a successful response arrived',
    () => {
      httpClient.get(url).subscribe(() => {
        expect(loaderServiceSpy.start).toHaveBeenCalledTimes(1);
        expect(loaderServiceSpy.stopAll).toHaveBeenCalledTimes(1);
      });
      httpTestingController.expectOne(url).flush({});
    });

  it('should call <loader_service>#stopAll when the <loader_service>#start has been called previously and an unsuccessful response arrived',
    () => {
      const error: HttpErrorResponse = new HttpErrorResponse({ status: HttpStatusCode.UNAUTHORIZED, statusText: 'Server Error', url: url });
      httpClient.get(url).subscribe(() => { }, () => {
        // error
        expect(loaderServiceSpy.start).toHaveBeenCalledTimes(1);
        expect(loaderServiceSpy.stopAll).toHaveBeenCalledTimes(1);
      });
      httpTestingController.expectOne(url).flush({}, error);
    });
});
