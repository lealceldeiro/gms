import { fakeAsync, inject, TestBed, tick } from '@angular/core/testing';

import { StorageService } from './storage.service';
import { CookieService } from 'ngx-cookie';
import { BehaviorSubject, Observable } from 'rxjs/index';
import { LocalStorage } from '@ngx-pwa/local-storage';

describe('StorageService', () => {
  let storageService: StorageService;

  // region mocks
  const key = 'sampleKey';
  const stringValue = 'sampleValue';
  const objectValue = { mKey: 'mKey' };
  const objectValue$ = new BehaviorSubject(objectValue).asObservable();
  const boolObs$ = new BehaviorSubject(true).asObservable();
  const error$ = Observable.create(observer => { observer.error(new Error('test error')); observer.complete(); });
  let setItemFn;
  function setItemFailed(): Observable<boolean> { return error$; }
  let getCookieNotNull;
  let getCookieObjectNotNull;
  function getCookieNull() { return null; }
  function getCookieObjectNull() { return null; }

  const spies = {
    ls: {
      set: function(a, b) {},
      get: function(a) {},
      clear: function() {},
      removeItem: function(a) {}
    },
    ck: {
      put: function(a, b, c) {},
      putObject: function(a, b, c) {},
      get: function(a) {},
      getAll: function() {},
      getObject: function(a) {},
      remove: function(a, b) {},
      removeAll: function(a) {}
    }
  };
  const localStorageMock = {
    setItem: function(): Observable<boolean> { spies.ls.set(arguments[0], arguments[1]); return setItemFn(); },
    getItem: function(): Observable<any> { spies.ls.get(arguments[0]); return objectValue$; },
    clear: function(): Observable<any> { spies.ls.clear(); return boolObs$; },
    removeItem: function(): Observable<any> { spies.ls.removeItem(arguments[0]); return boolObs$; },
  };
  const cookiesMock = {
    put: function(): void { spies.ck.put(arguments[0], arguments[1], arguments[2]); },
    putObject: function(): void { spies.ck.putObject(arguments[0], arguments[1], arguments[2]); },
    get: function(): string { spies.ck.get(arguments[0]); return getCookieNotNull(); },
    getObject: function(): Object { spies.ck.getObject(arguments[0]); return getCookieObjectNotNull(); },
    getAll: function(): Object { spies.ck.getAll(); return objectValue; },
    remove: function(): void { spies.ck.remove(arguments[0], arguments[1]); },
    removeAll: function(): void { spies.ck.removeAll(arguments[0]); },
  };
  // endregion

  // region spies
  let setItemSpy: jasmine.Spy;
  let getItemSpy: jasmine.Spy;
  let clearItemSpy: jasmine.Spy;
  let removeItemSpy: jasmine.Spy;

  let putSpy: jasmine.Spy;
  let putObjectSpy: jasmine.Spy;
  let getSpy: jasmine.Spy;
  let getAllSpy: jasmine.Spy;
  let getObjectSpy: jasmine.Spy;
  let removeSpy: jasmine.Spy;
  let removeAllSpy: jasmine.Spy;

  let consoleWarnSpy: jasmine.Spy;
  // endregion

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        StorageService,
        { provide: CookieService, useValue: cookiesMock },
        { provide: LocalStorage, useValue: localStorageMock }
      ]
    });
    storageService = TestBed.get(StorageService);
    setItemFn = function(): Observable<boolean> { spies.ls.set(arguments[0], arguments[1]); return boolObs$; };
    getCookieNotNull = function () { return stringValue; };
    getCookieObjectNotNull = function () { return objectValue; };

    // spies
    setItemSpy = spyOn(spies.ls, 'set');
    getItemSpy = spyOn(spies.ls, 'get');
    clearItemSpy = spyOn(spies.ls, 'clear');
    removeItemSpy = spyOn(spies.ls, 'removeItem');

    putSpy = spyOn(spies.ck, 'put');
    putObjectSpy = spyOn(spies.ck, 'putObject');
    getSpy = spyOn(spies.ck, 'get');
    getAllSpy = spyOn(spies.ck, 'getAll');
    getObjectSpy = spyOn(spies.ck, 'getObject');
    removeSpy = spyOn(spies.ck, 'remove');
    removeAllSpy = spyOn(spies.ck, 'removeAll');

    consoleWarnSpy = spyOn(console, 'warn');
  });

  it('should be created', inject([StorageService], (service: StorageService) => {
    expect(service).toBeTruthy();
  }));

  it('should throw an error if the key is `null` or `undefined`', () => {
    let threw = false;
    try {
      storageService['checkKey'](null);
    } catch (e) {
      threw = true;
    }
    expect(threw).toBeTruthy('did not throw when `null`');
    threw = false;
    try {
      storageService['checkKey'](undefined);
    } catch (e) {
      threw = true;
    }
    expect(threw).toBeTruthy('did not throw when `undefined`');
  });

  // region localStorage
  it('should set the value specified under a specified key', () => {
    // fresh set
    let reset = false;
    const val = storageService.set(key, objectValue);
    expect(val).toEqual(objectValue, 'set should return the set value properly');
    // cache checks
    const uk = storageService['gmsLs'] + key;
    expect(storageService['cache'][uk]).toBeTruthy('Behavior subject not set properly for ' + key);
    expect(storageService['cache$'][uk]).toBeTruthy('Observable not set properly for ' + key);
    storageService['cache$'][uk].subscribe(newVal => {
      if (!reset) {
        expect(newVal)
          .toEqual(objectValue, 'cache not set for ' + key + ': ' + JSON.stringify(objectValue));
      } else {
        expect(newVal)
          .toEqual(stringValue, 'cache not set for ' + key + ': ' + stringValue);
      }
    });

    const args = setItemSpy.calls.allArgs();
    expect(args[0][0]).toEqual(uk);
    expect(args[0][1]).toEqual(objectValue);

    // re-set
    reset = true;
    storageService.set(key, stringValue);
  });

  it('should re-try to set the value 2 times more if the first time it fails', fakeAsync(() => {
    setItemFn = setItemFailed;
    const ob = 'test';
    const uk = storageService['gmsLs'] + key;
    storageService.set(key, ob);
    tick();
    expect(setItemSpy).toHaveBeenCalledTimes(3);
    expect(consoleWarnSpy).toHaveBeenCalledTimes(1);
    expect(consoleWarnSpy.calls.allArgs()[0][0]).toEqual('Couldn\'t set ' + ob + ' under key \'' + uk + '\'');
  }));

  it('should get an Observable with the value specified under the key (value is cached)', () => {
    const uk = storageService['gmsLs'] + key;
    storageService['cache$'][uk] = new BehaviorSubject<any>(objectValue).asObservable();
    storageService.get(key).subscribe(val => expect(val).toEqual(objectValue));
  });

  it('should get an Observable with the value specified under the key (value is NOT cached)', () => {
    storageService.get(key).subscribe(val => expect(val).toEqual(objectValue));

    const args = getItemSpy.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsLs'] + key);
  });

  it('should clear all elements in the cache and clear the localStorage', () => {
    // mock values in cache
    const uk = storageService['gmsLs'] + key;
    storageService['cache'][uk] = new BehaviorSubject<string>(stringValue);
    storageService['cache$'][uk] = (storageService['cache'][uk] as BehaviorSubject<string>).asObservable();
    storageService.clear().subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      for (const k in storageService['cache']) {
        if (storageService['cache'].hasOwnProperty(k)) {
          expect((storageService['cache'][k] as BehaviorSubject<any>).getValue()).toBeNull();
        }
      }
    });
    expect(clearItemSpy).toHaveBeenCalled();
  });

  it('should clear the element saved under a key in both the cache and localStorage', () => {
    // mock values in cache
    const uk = storageService['gmsLs'] + key;
    storageService['cache'][uk] = new BehaviorSubject<any>(objectValue);
    storageService['cache$'][uk] = (storageService['cache'][uk] as BehaviorSubject<any>).asObservable();

    storageService.clear(key).subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      expect((storageService['cache'][uk] as BehaviorSubject<any>).getValue()).toBeNull();
    });
    expect(removeItemSpy).toHaveBeenCalled();
  });
  // endregion

  // region cookies
  it('should put the value specified under a specified key', () => {
    // fresh set
    let reset = false;
    const val = storageService.putCookie(key, objectValue);
    expect(val).toEqual(objectValue, '`putCookie` should return the set value properly');
    // cache checks
    const uk = storageService['gmsCk'] + key;
    expect(storageService['cacheCk'][uk]).toBeTruthy('Behavior subject not set properly for ' + key);
    expect(storageService['cacheCk$'][uk]).toBeTruthy('Observable not set properly for ' + key);
    storageService['cacheCk$'][uk].subscribe(newVal => {
      if (!reset) {
        expect(newVal)
          .toEqual(objectValue, 'cache not set for ' + key + ': ' + JSON.stringify(objectValue));
      } else {
        expect(newVal)
          .toEqual(stringValue, 'cache not set for ' + key + ': ' + stringValue);
      }
    });

    const args = putObjectSpy.calls.allArgs();
    expect(args[0][0]).toEqual(uk);
    expect(args[0][1]).toEqual(objectValue);

    // re-set (with a string value now)
    reset = true;
    storageService.putCookie(key, stringValue);
  });

  it('should get an Observable with the value (object) specified under the key (value is cached)', () => {
    const uk = storageService['gmsCk'] + key;
    storageService['cacheCk$'][uk] = new BehaviorSubject<any>(objectValue).asObservable();
    storageService.getCookie(key, true).subscribe(val => expect(val).toEqual(objectValue));
  });

  it('should get an Observable with the value (string) specified under the key (value is cached)', () => {
    const uk = storageService['gmsCk'] + key;
    storageService['cacheCk$'][uk] = new BehaviorSubject<string>(stringValue).asObservable();
    storageService.getCookie(key).subscribe(val => expect(val).toEqual(stringValue));
  });

  it('should get an Observable with the value (object) specified under the key (value is NOT cached)', () => {
    storageService.getCookie(key, true).subscribe(val => expect(val).toEqual(objectValue));

    const args = getObjectSpy.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should get an Observable with the value (string) specified under the key (value is NOT cached)', () => {
    storageService.getCookie(key).subscribe(val => expect(val).toEqual(stringValue));

    const args = getSpy.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should get an Observable with all the values (no key provided) (values are NOT cached)', () => {
    storageService.getCookie().subscribe(val => expect(val).toEqual(objectValue));
    expect(getAllSpy).toHaveBeenCalled();
  });

  it('should get an Observable with all the values (no key provided) (values are in cached, cache is updated ' +
    'with a new value (mocked one)',
    () => {
    const uk = storageService['gmsPriv'] + storageService['gmsCk'];
    storageService['cacheCk'][uk] = new BehaviorSubject<object>({ testKey: 'testVal' });
    storageService['cacheCk$'][uk] = storageService['cacheCk'][uk].asObservable();
    storageService.getCookie().subscribe(val => expect(val).toEqual(objectValue));
    expect(getAllSpy).toHaveBeenCalled();
  });

  it('should get an Observable with the `null` value when there is no specified value(string) under the key ' +
    '(and it is NOT cached)', () => {
    getCookieNotNull = getCookieNull;
    storageService.getCookie(key).subscribe(val => expect(val).toBeNull());

    const args = getSpy.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should get an Observable with the `null` value when there is no specified value(object) under the key ' +
    '(and it is NOT cached)', () => {
    getCookieObjectNotNull = getCookieObjectNull;
    storageService.getCookie(key, true).subscribe(val => expect(val).toBeNull());

    const args = getObjectSpy.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should clear both cookies and the cache', () => {
    // mock values in cache
    const uk = storageService['gmsCk'] + key;
    storageService['cacheCk'][uk] = new BehaviorSubject<string>(stringValue);
    storageService['cacheCk$'][uk] = (storageService['cacheCk'][uk] as BehaviorSubject<string>).asObservable();
    storageService.clearCookie().subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      for (const k in storageService['cacheCk']) {
        if (storageService['cacheCk'].hasOwnProperty(k)) {
          expect((storageService['cacheCk'][k] as BehaviorSubject<any>).getValue()).toBeNull();
        }
      }
    });
    expect(removeAllSpy).toHaveBeenCalled();
  });

  it('should clear the element saved under a key in both the cache and the cookies', () => {
    // mock values in cache
    const uk = storageService['gmsCk'] + key;
    storageService['cacheCk'][uk] = new BehaviorSubject<string>(stringValue);
    storageService['cacheCk$'][uk] = (storageService['cacheCk'][uk] as BehaviorSubject<string>).asObservable();

    storageService.clearCookie(key).subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      expect((storageService['cacheCk'][uk] as BehaviorSubject<string>).getValue()).toBeNull();
    });
    expect(removeSpy).toHaveBeenCalled();
  });
  // endregion
});
