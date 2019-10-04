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
  const userId = getRandomNumber();
  const user = new User();
  user.id = userId;
  let sessionServiceSpy: jasmine.SpyObj<SessionService>;
  let configurationService: ConfigurationService;
  let httpClientSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get']);
    httpClientSpy.get.and.returnValue(of(true));

    sessionServiceSpy = jasmine.createSpyObj('SessionService', ['getUser']);
    sessionServiceSpy.getUser.and.returnValue(new BehaviorSubject<User>(user).asObservable());

    TestBed.configureTestingModule({
      providers: [
        ConfigurationService, AppConfig,
        { provide: HttpClient, useValue: httpClientSpy },
        { provide: SessionService, useValue: sessionServiceSpy }
      ]
    });
    AppConfig.settings = MockAppConfig.settings;
    configurationService = TestBed.get(ConfigurationService);
  });

  it('should be created', () => {
    const service: ConfigurationService = TestBed.get(ConfigurationService);
    expect(service).toBeTruthy();
  });

  it('#getConfigurations should call HttpClient#get', () => {
    configurationService.getConfigurations();
    expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(url + 'configuration');
  });

  it('#getUserConfigurations should call SessionService#getUser to get the user id and HttpClient#get', () => {
    configurationService.getUserConfigurations().subscribe(() => {
      expect(sessionServiceSpy.getUser).toHaveBeenCalledTimes(1);
      expect(httpClientSpy.get).toHaveBeenCalledTimes(1);
      expect(httpClientSpy.get).toHaveBeenCalledWith(`${url}configuration/${userId}?human=true` );
    });
  });
});
