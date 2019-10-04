import { fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { LocalStorage } from '@ngx-pwa/local-storage';
import { CookieService } from 'ngx-cookie';
import { BehaviorSubject, Observable } from 'rxjs';

import { StorageService } from './storage.service';

describe('StorageService', () => {
  const key = 'sampleKey';
  const stringValue = 'sampleValue';
  const objectValue = { mKey: 'mKey' };
  const objectValue$ = new BehaviorSubject(objectValue).asObservable();
  const boolObs$ = new BehaviorSubject(true).asObservable();
  const error$ = new Observable<boolean>(observer => { observer.error(new Error('test error')); observer.complete(); });

  let localStorageSpy: jasmine.SpyObj<LocalStorage>;
  let cookieServiceSpy: jasmine.SpyObj<CookieService>;
  let consoleWarnSpy: jasmine.Spy;

  let storageService: StorageService;

  beforeEach(() => {
    cookieServiceSpy = jasmine.createSpyObj('CookieService', ['put', 'putObject', 'get', 'getObject', 'getAll', 'remove', 'removeAll']);
    cookieServiceSpy.get.and.returnValue(stringValue);
    cookieServiceSpy.getObject.and.returnValue(objectValue);
    cookieServiceSpy.getAll.and.returnValue(objectValue);

    localStorageSpy = jasmine.createSpyObj('LocalStorage', ['setItem', 'getItem', 'clear', 'removeItem']);
    localStorageSpy.setItem.and.returnValue(boolObs$);
    localStorageSpy.getItem.and.returnValue(objectValue$);
    localStorageSpy.clear.and.returnValue(boolObs$);
    localStorageSpy.removeItem.and.returnValue(boolObs$);

    TestBed.configureTestingModule({
      providers: [
        StorageService,
        { provide: CookieService, useValue: cookieServiceSpy },
        { provide: LocalStorage, useValue: localStorageSpy }
      ]
    });
    storageService = TestBed.get(StorageService);

    consoleWarnSpy = spyOn(console, 'warn');
  });

  it('should be created', inject([StorageService], (service: StorageService) => {
    expect(service).toBeTruthy();
  }));

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
    storageService['cache$'][uk].subscribe((newVal: any) => valuePutterSetterChecker(reset, newVal));

    const args = localStorageSpy.setItem.calls.allArgs();
    expect(args[0][0]).toEqual(uk);
    expect(args[0][1]).toEqual(objectValue);

    // re-set
    reset = true;
    storageService.set(key, stringValue);
  });

  it('should re-try to set the value 2 times more if the first time it fails', fakeAsync(() => {
    localStorageSpy.setItem.and.returnValue(error$);
    const ob = 'test';
    const uk = storageService['gmsLs'] + key;
    storageService.set(key, ob);
    tick();
    expect(localStorageSpy.setItem).toHaveBeenCalledTimes(3);
    expect(consoleWarnSpy).toHaveBeenCalledTimes(1);
    expect(consoleWarnSpy.calls.allArgs()[0][0]).toEqual(`Couldn't set ${ob} under key ${uk}`);
  }));

  it('should get an Observable with the value specified under the key (value is cached)', () => {
    const uk = storageService['gmsLs'] + key;
    storageService['cache$'][uk] = new BehaviorSubject<any>(objectValue).asObservable();
    storageService.get(key).subscribe(val => expect(val).toEqual(objectValue));
  });

  it('should get an Observable with the value specified under the key (value is NOT cached)', () => {
    storageService.get(key).subscribe(val => expect(val).toEqual(objectValue));

    const args = localStorageSpy.getItem.calls.allArgs();
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
    expect(localStorageSpy.clear).toHaveBeenCalled();
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
    expect(localStorageSpy.removeItem).toHaveBeenCalled();
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
    storageService['cacheCk$'][uk].subscribe((newVal: any) => valuePutterSetterChecker(reset, newVal));

    const args = cookieServiceSpy.putObject.calls.allArgs();
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

    const args = cookieServiceSpy.getObject.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should get an Observable with the value (string) specified under the key (value is NOT cached)', () => {
    storageService.getCookie(key).subscribe(val => expect(val).toEqual(stringValue));

    const args = cookieServiceSpy.get.calls.allArgs();
    expect(args[0][0]).toEqual(storageService['gmsCk'] + key);
  });

  it('should get an Observable with all the values (no key provided) (values are NOT cached)', () => {
    storageService.getCookie().subscribe(val => expect(val).toEqual(objectValue));
    expect(cookieServiceSpy.getAll).toHaveBeenCalled();
  });

  it('should get an Observable with all the values (no key provided) (values are in cached, cache is updated ' +
    'with a new value (mocked one)',
    () => {
      const uk = storageService['gmsPriv'] + storageService['gmsCk'];
      storageService['cacheCk'][uk] = new BehaviorSubject<object>({ testKey: 'testVal' });
      storageService['cacheCk$'][uk] = storageService['cacheCk'][uk].asObservable();
      storageService.getCookie().subscribe(val => expect(val).toEqual(objectValue));
      expect(cookieServiceSpy.getAll).toHaveBeenCalled();
    });

  it('should get an Observable with the `null` value when there is no specified value(object) under the key ' +
    '(and it is NOT cached)', () => {
      const sampleValue = { mKey: 'randomMKey' };
      cookieServiceSpy.getObject.and.returnValue(sampleValue);
      storageService.getCookie(key, true).subscribe(val => expect(val).toEqual(sampleValue));

      const args = cookieServiceSpy.getObject.calls.allArgs();
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
    expect(cookieServiceSpy.removeAll).toHaveBeenCalled();
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
    expect(cookieServiceSpy.remove).toHaveBeenCalled();
  });
  // endregion

  const valuePutterSetterChecker = (isReset: boolean, newVal: any) => {
    if (!isReset) {
      expect(newVal)
        .toEqual(objectValue, `cache not set for ${key}: ${JSON.stringify(objectValue)}`);
    } else {
      expect(newVal)
        .toEqual(stringValue, `cache not set for ${key }: ${stringValue}`);
    }
  };
});
