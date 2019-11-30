import { HttpClientTestingModule } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';

import { BehaviorSubject, Observable, of } from 'rxjs';

import { getRandomNumber } from '../../shared/test-util/functions.util';
import { MockAppConfig } from '../../shared/test-util/mock/app.config';
import { AppConfig } from '../config/app.config';
import { ISecurityKey } from '../model/config/app-config.model';
import { TruthyPredicate } from '../predicate/truthy.predicate';
import { StorageService } from '../storage/storage.service';
import { Util } from '../util/util';
import { LoginResponseModel } from './login-response.model';
import { SessionService } from './session.service';
import { userMock } from './user.mock.model';
import { User } from './user.model';

describe('SessionService', () => {
  const mockLoginResponse: LoginResponseModel = {
    access_token: 'atMock',
    header_to_be_sent: 'htbSMock',
    authorities: ['auth1, auth2'],
    issued_at: getRandomNumber(0, 9999999),
    refresh_token: 'rftMock',
    refresh_token_expiration_time: getRandomNumber(0, 9999),
    token_expiration_time: 9999999,
    token_type: 'typeTMock',
    username: 'userSample'
  };
  const subjectNull$ = new BehaviorSubject(null).asObservable();
  const trueVal$ = new BehaviorSubject(true).asObservable();

  let utilAllValuesFulfilSpy: jasmine.Spy;
  let storageServiceSpy: jasmine.SpyObj<StorageService>;
  let sessionService: SessionService;

  beforeEach(() => {
    storageServiceSpy = jasmine.createSpyObj('StorageService', ['set', 'get', 'clear', 'putCookie', 'getCookie', 'clearCookie']);
    storageServiceSpy.set.and.returnValue(subjectNull$);
    storageServiceSpy.get.and.returnValue(subjectNull$);
    storageServiceSpy.clear.and.returnValue(trueVal$);
    storageServiceSpy.putCookie.and.returnValue(subjectNull$);
    storageServiceSpy.getCookie.and.returnValue(subjectNull$);
    storageServiceSpy.clearCookie.and.returnValue(trueVal$);

    utilAllValuesFulfilSpy = spyOn(Util, 'allValuesFulfil').and.returnValue(true); // mock behavior for keys validity: make them valid

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService, { provide: StorageService, useValue: storageServiceSpy }]
    });
    sessionService = TestBed.get(SessionService);
  });

  it(`loadInitialData should call retrieve to get data for following keys: loggedInKey, notLoggedInKey, userKey,
    loginDataKey and rememberMeKey... and set the proper values to the observables holding this data`, () => {
    const value = `testValue${getRandomNumber()}`;
    const spy = { retrieve: (unused1: string, unused2: boolean) => { /* no-op */ } };
    const retrieveSpy = spyOn(spy, 'retrieve');
    sessionService['retrieve'] = (key: string, fromCookie: boolean): Observable<any> => {
      spy.retrieve(key, fromCookie);
      return of(value);
    };
    const valueInCookiesErrorMessage = 'is stored in cookies, it should be retrieved from cookies';
    const valueInStorageErrorMessage = 'is stored in storage, it should be retrieved from storage';
    const trueOrUndefined = [undefined, true];

    sessionService.loadInitialData();

    expect(retrieveSpy).toHaveBeenCalledTimes(5);

    expect(retrieveSpy.calls.argsFor(0)[0]).toBe(sessionService['loggedInKey']);
    expect(trueOrUndefined).toContain(retrieveSpy.calls.argsFor(0)[1], `loggedInKey ${valueInCookiesErrorMessage}`);

    expect(retrieveSpy.calls.argsFor(1)[0]).toBe(sessionService['notLoggedInKey']);
    expect(trueOrUndefined).toContain(retrieveSpy.calls.argsFor(1)[1], `notLoggedInKey ${valueInCookiesErrorMessage}`);

    expect(retrieveSpy.calls.argsFor(2)[0]).toBe(sessionService['userKey']);
    expect(retrieveSpy.calls.argsFor(2)[1]).toBe(false, `userKey ${valueInStorageErrorMessage}`);

    expect(retrieveSpy.calls.argsFor(3)[0]).toBe(sessionService['loginDataKey']);
    expect(retrieveSpy.calls.argsFor(3)[1]).toBe(false, `loginDataKey ${valueInStorageErrorMessage}`);

    expect(retrieveSpy.calls.argsFor(4)[0]).toBe(sessionService['rememberMeKey']);
    expect(trueOrUndefined).toContain(retrieveSpy.calls.argsFor(4)[1], `rememberMeKey ${valueInCookiesErrorMessage}`);
  });

  it('should be created', inject([SessionService], (service: SessionService) => {
    expect(service).toBeTruthy();
  }));

  describe('test default values', () => {
    it('default value for isLoggedIn should be `false`', (done) => {
      sessionService.isLoggedIn().subscribe((logged: boolean) => {
        expect(logged).toBeFalsy();
        done();
      });
    });

    it('default value for isNotLoggedIn should be `true`', (done) => {
      sessionService.isNotLoggedIn().subscribe((notLogged: boolean) => {
        expect(notLogged).toBeTruthy();
        done();
      });
    });

    it('default value for getUser should be empty default user (id = -1)', (done) => {
      sessionService.getUser().subscribe((user: User) => {
        expect(user).toEqual(new User());
        done();
      });
    });

    it('default value for getAuthData should be `{}`', () => {
      sessionService.getAuthData().subscribe((data: LoginResponseModel) => {
        expect(data).toEqual({});
      });
    });

    it('default value for isRememberMe should be `false`', (done) => {
      sessionService.isRememberMe().subscribe((r: boolean) => {
        expect((r)).toBe(false);
        done();
      });
    });

    it('default value for accessToken should be `empty string', (done) => {
      sessionService.getAccessToken().subscribe((token: string) => {
        expect((token)).toBe('');
        done();
      });
    });

    it('default value for refreshToken should be `empty string', (done) => {
      sessionService.getRefreshToken().subscribe((token: string) => {
        expect((token)).toBe('');
        done();
      });
    });

    it('default value for header should be `empty string', (done) => {
      sessionService.getHeader().subscribe((header: string) => {
        expect((header)).toBe('');
        done();
      });
    });

    it('default value for tokenType should be `empty string', (done) => {
      sessionService.getTokenType().subscribe((type: string) => {
        expect((type)).toBe('');
        done();
      });
    });
  });

  describe('test return values', () => {
    it('isLoggedIn should return the proper value regarding to `loggedIn$` observable (from )', (done) => {
      sessionService['loggedIn'].next(true);
      sessionService.isLoggedIn().subscribe((val: boolean) => {
        expect(val).toBeTruthy();
        done();
      });
    });

    it('isNotLoggedIn should return the proper value regarding to `notLoggedIn$` observable (from `notLoggedIn`)', (done) => {
      sessionService['notLoggedIn'].next(true);
      sessionService.isNotLoggedIn().subscribe((val: boolean) => {
        expect(val).toBeTruthy();
        done();
      });
    });

    it('getUser should return the proper value regarding to `user$` observable (from `user`)', (done) => {
      sessionService['user'].next(userMock);
      sessionService.getUser().subscribe((val: User) => {
        expect(val).toEqual(userMock);
        done();
      });
    });

    it('getAuthData should return the proper value regarding to `authData$` observable (from `authData`)', (done) => {
      const mock = { access_token: 'test', header_to_be_sent: 'headerTest' };
      sessionService['authData'].next(mock);
      sessionService.getAuthData().subscribe((val: any) => {
        expect(val).toEqual(mock);
        done();
      });
    });

    it('isRememberMe should return the proper value regarding to `rememberMe$` observable (from `rememberMe`)', (done) => {
      const value = getRandomNumber() % 2 === 0;
      sessionService['rememberMe'].next(value);
      sessionService.isRememberMe().subscribe((expected: boolean) => {
        expect(expected).toEqual(value);
        done();
      });
    });

    it('getAccessToken should return the proper value regarding to `accessToken$` observable (from `accessToken`)', (done) => {
      const value = `test${getRandomNumber()}value`;
      sessionService['accessToken'].next(value);
      sessionService.getAccessToken().subscribe((token: string) => {
        expect(token).toEqual(value);
        done();
      });
    });

    it('getRefreshToken should return the proper value regarding to `refreshToken$` observable (from `refreshToken`)', (done) => {
      const value = `test${getRandomNumber()}value`;
      sessionService['refreshToken'].next(value);
      sessionService.getRefreshToken().subscribe((token: string) => {
        expect(token).toEqual(value);
        done();
      });
    });

    it('getHeader should return the proper value regarding to `headerToBeSent$` observable (from `headerToBeSent`)', (done) => {
      const value = `test${getRandomNumber()}value`;
      sessionService['headerToBeSent'].next(value);
      sessionService.getHeader().subscribe((header: string) => {
        expect(header).toEqual(value);
        done();
      });
    });

    it('getTokenType should return the proper value regarding to `tokenType$` observable (from `tokenType`)', (done) => {
      const value = `token${getRandomNumber()}type`;
      sessionService['tokenType'].next(value);
      sessionService.getTokenType().subscribe((type: string) => {
        expect(type).toEqual(value);
        done();
      });
    });
  });

  describe('test set values', () => {
    it(`setLoggedIn should call the store service in order to save in localStorage the values regarding to
      'loggedIn' and 'notLoggedIn' and update the value of 'loggedIn' and 'notLoggedIn' subjects`, () => {
      setUpSessionServiceProperties();
      const value = getRandomNumber() % 2 === 0;
      sessionService.setLoggedIn(value);

      expect(storageServiceSpy.putCookie).toHaveBeenCalled();
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[0]).toBe(sessionService['loggedInKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[1]).toBe(value);
      expect(storageServiceSpy.putCookie.calls.argsFor(1)[0]).toBe(sessionService['notLoggedInKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(1)[1]).toBe(!value);
      expect(sessionService['loggedIn'].getValue()).toBe(value);
      expect(sessionService['notLoggedIn'].getValue()).toBe(!value);
    });
    it('setLoggedIn should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      const value = getRandomNumber() % 2 === 0;
      sessionService.setLoggedIn(value);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setUser should set the proper value regarding to `user` value and call the store service to save the value', () => {
      setUpSessionServiceProperties();

      sessionService.setUser(userMock);

      expect(storageServiceSpy.set).toHaveBeenCalled();
      expect(storageServiceSpy.set.calls.argsFor(0)[0]).toBe(sessionService['userKey']);
      expect(storageServiceSpy.set.calls.argsFor(0)[1]).toBe(userMock);
      expect(sessionService['user'].getValue()).toBe(userMock);
    });

    it('setUser should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setUser(userMock);

      expect(storageServiceSpy.set).not.toHaveBeenCalled();
    });

    it(`setAuthData should call the store service in order to save in localStorage the values regarding to
      'authData', 'accessToken', 'refreshToken', 'headerToBeSent' and 'tokenType' and update the value of
      'authData', 'accessToken', 'refreshToken', 'headerToBeSent' and 'tokenType' subjects`, () => {
      setUpSessionServiceProperties();

      sessionService.setAuthData(mockLoginResponse);

      expect(storageServiceSpy.set).toHaveBeenCalledTimes(1); // save login data in local storage
      expect(storageServiceSpy.putCookie).toHaveBeenCalledTimes(4); // save rest of the values in cookie

      // auth data
      expect(storageServiceSpy.set.calls.argsFor(0)[0]).toEqual(sessionService['loginDataKey']);
      expect(storageServiceSpy.set.calls.argsFor(0)[1]).toEqual(mockLoginResponse);
      expect(sessionService['authData'].getValue()).toEqual(mockLoginResponse);

      // access token
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[0]).toBe(sessionService['accessTokenKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[1]).toBe(mockLoginResponse.access_token);
      expect(sessionService['accessToken'].getValue()).toBe(mockLoginResponse.access_token as string);

      // refresh token
      expect(storageServiceSpy.putCookie.calls.argsFor(1)[0]).toBe(sessionService['refreshTokenKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(1)[1]).toBe(mockLoginResponse.refresh_token);
      expect(sessionService['refreshToken'].getValue()).toBe(mockLoginResponse.refresh_token as string);

      // header
      expect(storageServiceSpy.putCookie.calls.argsFor(2)[0]).toBe(sessionService['headerToBeSentKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(2)[1]).toBe(mockLoginResponse.header_to_be_sent);
      expect(sessionService['headerToBeSent'].getValue()).toBe(mockLoginResponse.header_to_be_sent as string);

      // token type
      expect(storageServiceSpy.putCookie.calls.argsFor(3)[0]).toBe(sessionService['tokenTypeKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(3)[1]).toBe(mockLoginResponse.token_type);
      expect(sessionService['tokenType'].getValue()).toBe(mockLoginResponse.token_type as string);
    });

    it('setAuthData should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setAuthData(mockLoginResponse);

      expect(storageServiceSpy.set).not.toHaveBeenCalled();
      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setAccessToken should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setAccessToken(`${getRandomNumber()}test`);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setRefreshToken should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setRefreshToken(`${getRandomNumber()}test`);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setHeader should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setHeader(`${getRandomNumber()}test`);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setTokenType should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      sessionService.setTokenType(`${getRandomNumber()}test`);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });

    it('setRememberMe should set the proper value regarding to `rememberMe` value', () => {
      setUpSessionServiceProperties();

      const value = getRandomNumber() % 2 === 2;
      sessionService.setRememberMe(value);

      expect(storageServiceSpy.putCookie).toHaveBeenCalled();
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[0]).toBe(sessionService['rememberMeKey']);
      expect(storageServiceSpy.putCookie.calls.argsFor(0)[1]).toBe(value);
      expect(sessionService['rememberMe'].getValue()).toBe(value);
    });

    it('setRememberMe should early return if the key is not valid', () => {
      setUpSessionServiceProperties();
      setKeyAreInvalid();

      const value = getRandomNumber() % 2 === 2;
      sessionService.setRememberMe(value);

      expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
    });
  });

  it(`closeSession should set loggedIn as 'false', 'notLoggedIn' as true, 'authData' as an empty object and
    the session user as an empty user`, () => {
    setUpSessionServiceProperties();
    const undefinedString: string = undefined as unknown as string;
    sessionService.closeSession();

    // region check session service vars
    expect(sessionService['loggedIn'].getValue()).toBeFalsy('`loggedIn` subject value was not set properly');
    expect(sessionService['notLoggedIn'].getValue()).toBeTruthy('`notLoggedIn` subject value was not set properly');
    expect(sessionService['authData'].getValue()).toEqual({}, '`authData` subject value was not set properly');
    expect(sessionService['accessToken'].getValue()).toBe(undefinedString, '`accessToken` subject value was not set properly');
    expect(sessionService['refreshToken'].getValue()).toBe(undefinedString, '`refreshToken` subject value was not set properly');
    expect(sessionService['headerToBeSent'].getValue()).toBe(undefinedString, '`headerToBeSent` subject value was not set properly');
    expect(sessionService['tokenType'].getValue()).toBe(undefinedString, '`tokenType` subject value was not set properly');
    expect(sessionService['user'].getValue()).toEqual(new User(), '`user` subject value was not set properly');
    // endregion

    expect(storageServiceSpy.clear).toHaveBeenCalledTimes(2);
    expect(storageServiceSpy.clearCookie).toHaveBeenCalledTimes(6);

    // region check args calls
    // region clear
    expect(storageServiceSpy.clear.calls.argsFor(0)[0]).toEqual(sessionService['loginDataKey'], 'incorrect key for `authData`');
    expect(storageServiceSpy.clear.calls.argsFor(1)[0]).toEqual(sessionService['userKey'], 'incorrect key for `user`');
    // endregion

    // region clearCookie
    expect(storageServiceSpy.clearCookie.calls.argsFor(0)[0]).toEqual(sessionService['loggedInKey'], 'incorrect key for `loggedIn`');
    expect(storageServiceSpy.clearCookie.calls.argsFor(1)[0])
      .toEqual(sessionService['notLoggedInKey'], 'incorrect key for `notLoggedIn`');
    expect(storageServiceSpy.clearCookie.calls.argsFor(2)[0])
      .toEqual(sessionService['accessTokenKey'], 'incorrect key for `accessToken`');
    expect(storageServiceSpy.clearCookie.calls.argsFor(3)[0])
      .toEqual(sessionService['refreshTokenKey'], 'incorrect key for `refreshToken`');
    expect(storageServiceSpy.clearCookie.calls.argsFor(4)[0])
      .toEqual(sessionService['headerToBeSentKey'], 'incorrect key for `headerToBeSent`');
    expect(storageServiceSpy.clearCookie.calls.argsFor(5)[0]).toEqual(sessionService['tokenTypeKey'], 'incorrect key for `tokenType`');
    // endregion
    // endregion
  });

  it('closeSession should early return if the keys are not valid', () => {
    setUpSessionServiceProperties();
    setKeyAreInvalid();

    sessionService.closeSession();

    expect(storageServiceSpy.clear).not.toHaveBeenCalledTimes(2);
    expect(storageServiceSpy.clearCookie).not.toHaveBeenCalledTimes(6);
  });

  it('store should early return if the key param is falsy', () => {
    const falseKey = false as unknown as string;
    const nullKey = null as unknown as string;
    const undefinedKey = undefined as unknown as string;
    const zeroKey = 0 as unknown as string;
    const value = `testValue${getRandomNumber()}`;
    const storeFn = sessionService['store'];

    storeFn(falseKey, value);
    storeFn(nullKey, value);
    storeFn(undefinedKey, value);
    storeFn(zeroKey, value);

    expect(storageServiceSpy.putCookie).not.toHaveBeenCalled();
  });

  describe('retrieve should call StorageService', () => {
    it(`#getCookie with the first argument provided to its call
      and 'false' as second argument, when second argument is not provided`, () => {
      const key = `test4${getRandomNumber()}`;
      sessionService['retrieve'](key);

      expect(storageServiceSpy.get).not.toHaveBeenCalled();
      expect(storageServiceSpy.getCookie).toHaveBeenCalled();
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[0]).toBe(key);
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[1]).toBe(false);
    });

    it(`#getCookie with the first argument provided to its call
      and 'false' as second argument, when second argument is true`, () => {
      const key = `test4${getRandomNumber()}`;
      sessionService['retrieve'](key, true);

      expect(storageServiceSpy.get).not.toHaveBeenCalled();
      expect(storageServiceSpy.getCookie).toHaveBeenCalled();
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[0]).toBe(key);
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[1]).toBe(false);
    });

    it(`#getCookie with the first argument provided to its call
      when second argument is true and 'true' as second argument if it is provided to its call`, () => {
      const key = `test5${getRandomNumber()}`;
      sessionService['retrieve'](key, true, true);

      expect(storageServiceSpy.get).not.toHaveBeenCalled();
      expect(storageServiceSpy.getCookie).toHaveBeenCalled();
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[0]).toBe(key);
      expect(storageServiceSpy.getCookie.calls.argsFor(0)[1]).toBe(true);
    });

    it('#get with the first argument provided to its call when second argument is false', () => {
      const key = `test5${getRandomNumber()}`;
      sessionService['retrieve'](key, false);

      expect(storageServiceSpy.getCookie).not.toHaveBeenCalled();
      expect(storageServiceSpy.get).toHaveBeenCalled();
      expect(storageServiceSpy.get.calls.argsFor(0)[0]).toBe(key);
    });
  });

  it('_key should return keys under AppConfig#settings', () => {
    AppConfig.settings = MockAppConfig.settings;
    const keys = Object.keys(MockAppConfig.settings.security.hash.key);

    keys.forEach(key => {
      expect(sessionService['_key'](key)).toBe(MockAppConfig.settings.security.hash.key[key as keyof ISecurityKey]);
    });
  });

  it(`keysAreValid should call Util#allValuesFulfil providing a truthy predicate instance and the provided keys as
    second argument`, () => {
    const keys: string[] = [];
    for (let i = 0; i < getRandomNumber(); i++) {
      keys.push(`key${getRandomNumber()}`);
    }

    expect(sessionService['keysAreValid'](...keys)).toBe(true); // true as mocked in beforeEach setup

    expect(utilAllValuesFulfilSpy).toHaveBeenCalled();
    expect(utilAllValuesFulfilSpy.calls.argsFor(0)[0]).toEqual(new TruthyPredicate<ISecurityKey | string>());

    keys.forEach((key, index) => {
      expect(utilAllValuesFulfilSpy.calls.argsFor(0)[index + 1]).toContain(key);
    });
  });

  describe('key getters should call _key with the proper value', () => {
    const spy = { _key: (unused: string) => { /* no-op */ } };
    let _keySpy: jasmine.Spy;
    let valueAssociatedToKey: string;

    beforeEach(() => {
      valueAssociatedToKey = `valueAssociatedToKey${getRandomNumber()}`;
      sessionService['_key'] = (k: string): string => {
        spy._key(k);
        return valueAssociatedToKey;
      };

      _keySpy = spyOn(spy, '_key');
    });

    it('loggedInKey', () => {
      testKeyGetter('loggedInKey', 'loggedIn', valueAssociatedToKey, _keySpy);
    });

    it('notLoggedInKey', () => {
      testKeyGetter('notLoggedInKey', 'notLoggedIn', valueAssociatedToKey, _keySpy);
    });

    it('userKey', () => {
      testKeyGetter('userKey', 'user', valueAssociatedToKey, _keySpy);
    });

    it('loginDataKey', () => {
      testKeyGetter('loginDataKey', 'loginData', valueAssociatedToKey, _keySpy);
    });

    it('accessTokenKey', () => {
      testKeyGetter('accessTokenKey', 'accessToken', valueAssociatedToKey, _keySpy);
    });

    it('refreshTokenKey', () => {
      testKeyGetter('refreshTokenKey', 'refreshToken', valueAssociatedToKey, _keySpy);
    });

    it('tokenTypeKey', () => {
      testKeyGetter('tokenTypeKey', 'tokenType', valueAssociatedToKey, _keySpy);
    });

    it('rememberMeKey', () => {
      testKeyGetter('rememberMeKey', 'rememberMe', valueAssociatedToKey, _keySpy);
    });

    it('headerToBeSentKey', () => {
      testKeyGetter('headerToBeSentKey', 'headerToBeSent', valueAssociatedToKey, _keySpy);
    });
  });

  function setUpSessionServiceProperties(config: { [key: string]: string } = {}) {
    Object.defineProperties(sessionService, {
      loggedInKey: {
        value: config.loggedInKey || `key${getRandomNumber()}test`,
        writable: false
      },
      notLoggedInKey: {
        value: config.notLoggedInKey || `key1${getRandomNumber()}test1`,
        writable: false
      },
      loginDataKey: {
        value: config.loginDataKey || `key2${getRandomNumber()}test2`,
        writable: false
      },
      accessTokenKey: {
        value: config.accessTokenKey || `key3${getRandomNumber()}test3`,
        writable: false
      },
      refreshTokenKey: {
        value: config.refreshTokenKey || `key4${getRandomNumber()}test4`,
        writable: false
      },
      headerToBeSentKey: {
        value: config.headerToBeSentKey || `key5${getRandomNumber()}test5`,
        writable: false
      },
      tokenTypeKey: {
        value: config.tokenTypeKey || `key6${getRandomNumber()}test6`,
        writable: false
      },
      userKey: {
        value: config.userKey || `key7${getRandomNumber()}test7`,
        writable: false
      },
      rememberMeKey: {
        value: config.rememberMeKey || `key8${getRandomNumber()}test8`,
        writable: false
      }
    });
  }

  function setKeyAreInvalid() {
    sessionService['keysAreValid'] = () => false;
  }

  function testKeyGetter(getterName: string, configKey: string, valueAssociatedToKey: string, _keySpy: jasmine.Spy) {
    expect(sessionService[getterName as keyof SessionService]).toBe(valueAssociatedToKey);
    expect(_keySpy).toHaveBeenCalled();
    expect(_keySpy.calls.argsFor(0)[0]).toBe(configKey);
  }
});
