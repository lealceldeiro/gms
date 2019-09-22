import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { ConfigurationService } from './configuration.service';
import { SessionService } from 'src/app/core/session/session.service';
import { BehaviorSubject, of } from 'rxjs';
import { User } from 'src/app/core/session/user.model';
import { getRandomNumber } from 'src/app/shared/test-util/functions.util';
import { AppConfig } from 'src/app/core/config/app.config';
import { MockAppConfig } from 'src/app/shared/test-util/mock/app.config';

describe('ConfigurationService', () => {
  const url = MockAppConfig.settings.apiServer.url;
  const httpSpy = { get: (_a: any, _b: any) => { } };
  const sessionSSpy = { getUser: () => { } };
  const httpClientMock = { get: (a: any, b: any) => { httpSpy.get(a, b); return of(true); } };
  const userId = getRandomNumber();
  const user = new User();
  user.id = userId;
  const userBehaviorSubject = new BehaviorSubject<User>(user);
  const sessionServiceMock = {
    getUser: () => { sessionSSpy.getUser(); return userBehaviorSubject.asObservable(); }
  };
  let configurationService: ConfigurationService;
  let httpClientGetSpy: jasmine.Spy;
  let sessionServiceSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ConfigurationService, AppConfig,
        { provide: HttpClient, useValue: httpClientMock },
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    });
    AppConfig.settings = MockAppConfig.settings;
    configurationService = TestBed.get(ConfigurationService);
    httpClientGetSpy = spyOn(httpSpy, 'get');
    sessionServiceSpy = spyOn(sessionSSpy, 'getUser');
  });

  it('should be created', () => {
    const service: ConfigurationService = TestBed.get(ConfigurationService);
    expect(service).toBeTruthy();
  });

  it('#getConfigurations should call HttpClient#get', () => {
    configurationService.getConfigurations();
    expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
    expect(httpClientGetSpy).toHaveBeenCalledWith(url + 'configuration', undefined);
  });

  it('#getUserConfigurations should call SessionService#getUser to get the user id and HttpClient#get', () => {
    configurationService.getUserConfigurations().subscribe(() => {
      expect(sessionServiceSpy).toHaveBeenCalledTimes(1);
      expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
      expect(httpClientGetSpy).toHaveBeenCalledWith(`${url}configuration/${userId}?human=true`, undefined );
    });
  });
});
