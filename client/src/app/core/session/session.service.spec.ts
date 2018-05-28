import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BehaviorSubject, Observable } from 'rxjs/index';

import { SessionService } from './session.service';
import { StorageService } from '../storage/storage.service';
import { User } from './user.model';
import { LoginResponseModel } from './login-response.model';
import { userMock } from './user.mock.model';
import Spy = jasmine.Spy;

describe('SessionService', () => {
  let storageServiceSetSpy: Spy;
  let storageServicePutCookieSpy: Spy;
  let storageServiceClearSpy: Spy;
  let storageServiceClearCookieSpy: Spy;
  let sessionService: SessionService;
  let storageService: StorageService;

  // region values for simulating a fresh store service
  const subjectNull$ = new BehaviorSubject(null).asObservable();
  const subjectEmpty$ = new BehaviorSubject({}).asObservable();
  const trueVal$ = new BehaviorSubject(true).asObservable();
  const testSpy = {
    set: function(a, b, c) {},
    clear: function(a, b, c) {},
    putCookie: function (a, b, c) {},
    clearCookie: function (a, b, c) {}
  };

  function dSet(): Observable<any> {
    testSpy.set(arguments[0], arguments[1], arguments[2]);
    return subjectNull$;
  }
  function dClear(): Observable<boolean> {
    testSpy.clear(arguments[0], arguments[1], arguments[2]);
    return trueVal$;
  }
  function dPutCookie(): Observable<any> {
    testSpy.putCookie(arguments[0], arguments[1], arguments[2]);
    return subjectNull$;
  }
  function d(): Observable<any> { return subjectNull$; }
  function dClearCookie(): Observable<boolean> {
    testSpy.clearCookie(arguments[0], arguments[1], arguments[2]);
    return trueVal$;
  }

  const storageServiceStub = {
    set: dSet,
    get: d,
    clear: dClear,
    putCookie: dPutCookie,
    getCookie: d,
    clearCookie: dClearCookie,
  };
  // endregion

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService, { provide: StorageService, useValue: storageServiceStub }]
    });
    sessionService = TestBed.get(SessionService);
    storageService = TestBed.get(StorageService);
    storageServiceSetSpy = spyOn(testSpy, 'set');
    storageServicePutCookieSpy = spyOn(testSpy, 'putCookie');
    storageServiceClearSpy = spyOn(testSpy, 'clear');
    storageServiceClearCookieSpy = spyOn(testSpy, 'clearCookie');
  });

  it('should be created', inject([SessionService], (service: SessionService) => {
    expect(service).toBeTruthy();
  }));

  it('default value for isLoggedIn be `false`', () => {
    sessionService.isLoggedIn().subscribe((logged: boolean) => {
      expect(logged).toBeFalsy('');
    });
  });

  it('default value for isNotLoggedIn should be `true`', () => {
    sessionService.isNotLoggedIn().subscribe((notLogged: boolean) => {
      expect(notLogged).toBeTruthy();
    });
  });

  it('default value for getUser should be `null`', () => {
    sessionService.getUser().subscribe((user: User) => {
      expect(user).toBeNull();
    });
  });

  it('default value for getAuthData should be `{}`', () => {
    storageService.get = function(): Observable<any> { return subjectEmpty$; };
    sessionService.getAuthData().subscribe((data: LoginResponseModel) => {
      expect(data).toEqual({}, '');
    });
  });

  it('default value for isRememberMe should be `false`', () => {
    expect(sessionService.isRememberMe()).toBeFalsy();
  });

  it('isLoggedIn should return the proper value regarding to `loggedIn$` observable', () => {
    sessionService['loggedIn'].next(true);
    sessionService.isLoggedIn().subscribe((val: boolean) => expect(val).toBeTruthy());
  });

  it('isNotLoggedIn should return the proper value regarding to `notLoggedIn$` observable', () => {
    sessionService['notLoggedIn'].next(true);
    sessionService.isNotLoggedIn().subscribe((val: boolean) => expect(val).toBeTruthy());
  });

  it('getUser should return the proper value regarding to `user$` observable', () => {
    sessionService['user'].next(userMock);
    sessionService.getUser().subscribe((val: User) => expect(val).toEqual(userMock));
  });

  it('getAuthData should return the proper value regarding to `authData$` observable', () => {
    const mock = {access_token: 'test', header_to_be_sent: 'headerTest'};
    sessionService['authData'].next(mock);
    sessionService.getAuthData().subscribe((val: any) => expect(val).toEqual(mock));
  });

  it ('setLoggedIn should call the store service in order to save in localStorage the values regarding to' +
    '`loggedIn` and `notLoggedIn` and update the value of `loggedIn` and `notLoggedIn` subjects', () => {
    sessionService.setLoggedIn(true);
    const allArgs = storageServiceSetSpy.calls.allArgs(); // array of array of args
    for (let i = 0; i < allArgs.length; i++) {
      if (allArgs[i][0] === sessionService['key']['loggedIn']) {
        expect(allArgs[i][1]).toBeTruthy();
      } else if (allArgs[i][0] === sessionService['key']['notLoggedIn']) {
        expect(allArgs[i][1]).toBeFalsy();
      }
    }
    expect(sessionService['loggedIn'].getValue()).toBeTruthy('set for loggedIn did not work');
    expect(sessionService['notLoggedIn'].getValue()).toBeFalsy('set for notLoggedIn did not work');
  });

  it ('setUser should call the store service in order to save in localStorage the values regarding to' +
    '`user` and update the value of `user` subject', () => {
    sessionService.setUser(userMock);
    const allArgs = storageServiceSetSpy.calls.allArgs(); // array of array of args
    expect(allArgs[0][0]).toEqual(sessionService['key']['user']);
    expect(allArgs[0][1]).toEqual(userMock);
    expect(sessionService['user'].getValue()).toEqual(userMock, 'set for user did not work');
  });

  it ('setAuthData should call the store service in order to save in localStorage the values regarding to' +
    '`loginData` and update the value of `loginData` subject', () => {
    const mock: LoginResponseModel = {
      access_token: 'atMock',
      header_to_be_sent: 'htbSMock',
      authorities: ['auth1, auth2'],
      issued_at: 1212112,
      refresh_token: 'rftMock',
      refresh_token_expiration_time: 1212,
      token_expiration_time: 2321323,
      token_type: 'typeTMock',
      username: 'userSample',
    };
    sessionService.setAuthData(mock);
    const allArgs = storageServiceSetSpy.calls.allArgs(); // array of array of args
    expect(allArgs[0][0]).toEqual(sessionService['key']['loginData']);
    expect(allArgs[0][1]).toEqual(mock);
    expect(sessionService['authData'].getValue()).toEqual(mock, 'set for authData did not work');
  });

  it('isRememberMe should return the proper value regarding to `rememberMe` value', () => {
    sessionService['rememberMe'] = true;
    expect(sessionService.isRememberMe()).toBeTruthy();
    sessionService['rememberMe'] = false;
    expect(sessionService.isRememberMe()).toBeFalsy();
  });

  it('setRememberMe should set the proper value regarding to `rememberMe` value', () => {
    sessionService.setRememberMe(true);
    expect(sessionService['rememberMe']).toBeTruthy();
    sessionService.setRememberMe(false);
    expect(sessionService['rememberMe']).toBeFalsy();
  });

  it('closeSession should set loggedIn as `false`, `notLoggedIn` as true, `authData` as empty and the session' +
    ' user as `null`', () => {
    sessionService.closeSession();

    // region check session service vars
    expect(sessionService['loggedIn'].getValue())
      .toBeFalsy('`loggedIn` subject value was not set properly');
    expect(sessionService['notLoggedIn'].getValue())
      .toBeTruthy('`notLoggedIn` subject value was not set properly');
    expect(sessionService['authData'].getValue())
      .toEqual({}, '`authData` subject value was not set properly');
    expect(sessionService['user'].getValue())
      .toBeNull('`user` subject value was not set properly');
    // endregion

    // region check args calls
    // region clear
    const allArgs = storageServiceClearSpy.calls.allArgs();

    expect(allArgs[0][0]).toEqual(sessionService['key']['loggedIn'],
      'incorrect key for `loggedIn`');

    expect(allArgs[1][0]).toEqual(sessionService['key']['notLoggedIn'],
      'incorrect key for `notLoggedIn`');

    expect(allArgs[2][0]).toEqual(sessionService['key']['loginData'],
      'incorrect key for `authData`');

    expect(allArgs[3][0]).toEqual(sessionService['key']['user'],
      'incorrect key for `user`');
    // endregion

    // region clearCookie
    const allArgsCk = storageServiceClearCookieSpy.calls.allArgs();

    expect(allArgsCk[0][0]).toEqual(sessionService['key']['accessToken'],
      'incorrect key for `accessToken`');

    expect(allArgsCk[1][0]).toEqual(sessionService['key']['refreshToken'],
      'incorrect key for `refreshToken`');

    expect(allArgsCk[2][0]).toEqual(sessionService['key']['headerToBeSent'],
      'incorrect key for `headerToBeSent`');

    expect(allArgsCk[3][0]).toEqual(sessionService['key']['tokenType'],
      'incorrect key for `tokenType`');
    // endregion
    // endregion
  });
});
