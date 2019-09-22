import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { environment } from 'src/environments/environment';
import { ConfigurationService } from './configuration.service';
import { SessionService } from 'src/app/core/session/session.service';
import { BehaviorSubject } from 'rxjs';
import { User } from 'src/app/core/session/user.model';
import { getRandomNumber } from 'src/app/shared/test-util/functions.util';

describe('ConfigurationService', () => {
  const httpSpy = { get: (_a: any, _b: any) => { } };
  const sessionSSpy = { getUser: () => { } };
  const httpClientMock = { get: (a: any, b: any) => httpSpy.get(a, b) };
  const userBehaviorSubject = new BehaviorSubject<User>(new User());
  const sessionServiceMock = {
    getUser: () => { sessionSSpy.getUser(); return userBehaviorSubject.asObservable(); }
  };
  let configurationService: ConfigurationService;
  let httpClientGetSpy: jasmine.Spy;
  let sessionServiceSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ConfigurationService,
        { provide: HttpClient, useValue: httpClientMock },
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    });
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
    expect(httpClientGetSpy).toHaveBeenCalledWith(environment.apiBaseUrl + 'configuration', undefined);
  });

  it('#getUserConfigurations should call SessionService#getUser to get the user id and HttpClient#get', () => {
    const sub = configurationService.getUserConfigurations().subscribe(() => {
      expect(sessionServiceSpy).toHaveBeenCalledTimes(1);
      expect(httpClientGetSpy).toHaveBeenCalledTimes(1);
      expect(httpClientGetSpy).toHaveBeenCalledWith(`${environment.apiBaseUrl}configuration/${id}`, undefined);
      sub.unsubscribe();
    });
    const id = getRandomNumber();
    const user = new User();
    user.id = id;
    userBehaviorSubject.next(user);
  });
});
