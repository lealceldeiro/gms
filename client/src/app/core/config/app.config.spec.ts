import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { inject, TestBed, fakeAsync, tick } from '@angular/core/testing';

import { AppConfig } from './app.config';
import { MockAppConfig } from 'src/app/shared/test-util/mock/app.config';

describe('AppConfig', () => {
  let appConfig: AppConfig;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppConfig]
    });
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    appConfig = TestBed.get(AppConfig);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', inject([AppConfig], (service: AppConfig) => {
    expect(service).toBeTruthy();
  }));

  it('should call HttpClient#get to get the configuration values and set `AppConfig.settings`', fakeAsync(() => {
    appConfig.load()
      .then(() => expect(AppConfig.settings).toBeTruthy())
      .catch(error => expect(error).toBeFalsy());

    const req = httpTestingController.expectOne(r => r.method === 'GET');
    req.flush(MockAppConfig.settings);
    tick();
  }));

  it('should call HttpClient#get to get the configuration values and report an error when the config files are not found', fakeAsync(() => {
    appConfig.load()
      .then(() => expect(AppConfig.settings).toBeFalsy())
      .catch(error => expect(error).toBeTruthy());

    const req = httpTestingController.expectOne(r => r.method === 'GET');
    req.error(new ErrorEvent('Example error'));
    tick();
  }));
});
