import { fakeAsync, inject, TestBed, tick } from '@angular/core/testing';

import { StorageMap } from '@ngx-pwa/local-storage';
import { CookieService } from 'ngx-cookie';
import { BehaviorSubject, Observable } from 'rxjs';

import { getRandomNumber } from '../../shared/test-util/functions.util';
import { StorageService } from './storage.service';

describe('StorageService', () => {
  const key = 'sampleKey';
  const stringValue = 'sampleValue';
  const objectValue = { mKey: 'mKey' };
  const objectValue$ = new BehaviorSubject(objectValue).asObservable();
  const undefinedObservable$ = new BehaviorSubject(undefined).asObservable();
  const error$ = new Observable<undefined>(observer => {
    observer.error(new Error('test error'));
    observer.complete();
  });

  let localStorageSpy: jasmine.SpyObj<StorageMap>;
  let cookieServiceSpy: jasmine.SpyObj<CookieService>;
  let consoleWarnSpy: jasmine.Spy;

  let storageService: StorageService;

  beforeEach(() => {
    cookieServiceSpy = jasmine.createSpyObj(
      'CookieService',
      ['put', 'putObject', 'get', 'getObject', 'getAll', 'remove', 'removeAll']
    );
    cookieServiceSpy.get.and.returnValue(stringValue);
    cookieServiceSpy.getObject.and.returnValue(objectValue);
    cookieServiceSpy.getAll.and.returnValue(objectValue);

    localStorageSpy = jasmine.createSpyObj('StorageMap', ['set', 'get', 'clear', 'delete']);
    localStorageSpy.set.and.returnValue(undefinedObservable$);
    localStorageSpy.get.and.returnValue(objectValue$);
    localStorageSpy.clear.and.returnValue(undefinedObservable$);
    localStorageSpy.delete.and.returnValue(undefinedObservable$);

    TestBed.configureTestingModule({
      providers: [
        StorageService,
        { provide: CookieService, useValue: cookieServiceSpy },
        { provide: StorageMap, useValue: localStorageSpy }
      ]
    });
    storageService = TestBed.get(StorageService);

    consoleWarnSpy = spyOn(console, 'warn');
  });

  it('should be created', inject([StorageService], (service: StorageService) => {
    expect(service).toBeTruthy();
  }));

  // region localStorage
  it('should set the value specified under a specified key', (done) => {
    // fresh set
    let reset = false;
    const val = storageService.set(key, objectValue);

    expect(val).toEqual(objectValue, 'set should return the set value properly');
    // cache checks
    expect(storageService['cache'][key]).toBeTruthy('Behavior subject not set properly for ' + key);
    expect(storageService['cache$'][key]).toBeTruthy('Observable not set properly for ' + key);
    storageService['cache$'][key].subscribe((newVal: any) => {
      valuePutterSetterChecker(reset, newVal);
      done();
    });

    const args = localStorageSpy.set.calls.allArgs();

    expect(args[0][0]).toEqual(key);
    expect(args[0][1]).toEqual(objectValue);

    // re-set
    reset = true;
    storageService.set(key, stringValue);
  });

  it('set should return null if the key is not valid', () => {
    storageService['isValidKey'] = () => false;

    expect(storageService.set(`key${ getRandomNumber() }`, `value${ getRandomNumber() }`)).toBe(null);
  });

  it('should re-try to set the value 2 times more if the first time it fails', fakeAsync(() => {
    localStorageSpy.set.and.returnValue(error$);
    const ob = 'test';
    storageService.set(key, ob);
    tick();
    expect(localStorageSpy.set).toHaveBeenCalledTimes(3);
    expect(consoleWarnSpy).toHaveBeenCalledTimes(1);
    expect(consoleWarnSpy.calls.allArgs()[0][0]).toEqual(`Couldn't set ${ ob } under key ${ key }`);
  }));

  it('should get an Observable with the value specified under the key (value is cached)', (done) => {
    storageService['cache$'][key] = new BehaviorSubject<any>(objectValue).asObservable();
    storageService.get(key).subscribe(val => {
      expect(val).toEqual(objectValue);
      done();
    });
  });

  it('should get an Observable with the value specified under the key (value is NOT cached)', () => {
    storageService.get(key).subscribe(val => expect(val).toEqual(objectValue));

    const args = localStorageSpy.get.calls.allArgs();
    expect(args[0][0]).toEqual(key);
  });

  it('should clear the element saved under a key in both the cache and localStorage', (done) => {
    // mock values in cache
    storageService['cache'][key] = new BehaviorSubject<any>(objectValue);
    storageService['cache$'][key] = (storageService['cache'][key] as BehaviorSubject<any>).asObservable();

    storageService.clear(key).subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      expect((storageService['cache'][key] as BehaviorSubject<any>).getValue()).toBeNull();
      done();
    });
    expect(localStorageSpy.delete).toHaveBeenCalled();
  });
  // endregion

  // region cookies
  it('should put the value specified under a specified key', (done) => {
    // fresh set
    let reset = false;
    const val = storageService.putCookie(key, objectValue);
    expect(val).toEqual(objectValue, '`putCookie` should return the set value properly');
    // cache checks
    expect(storageService['cacheCk'][key]).toBeTruthy('Behavior subject not set properly for ' + key);
    expect(storageService['cacheCk$'][key]).toBeTruthy('Observable not set properly for ' + key);
    storageService['cacheCk$'][key].subscribe((newVal: any) => {
      valuePutterSetterChecker(reset, newVal);
      done();
    });

    const args = cookieServiceSpy.putObject.calls.allArgs();
    expect(args[0][0]).toEqual(key);
    expect(args[0][1]).toEqual(objectValue);

    // re-set (with a string value now)
    reset = true;
    storageService.putCookie(key, stringValue);
  });

  it('putCookie should return null if the key is not valid', () => {
    storageService['isValidKey'] = () => false;

    expect(storageService.putCookie(`key${ getRandomNumber() }`, `value${ getRandomNumber() }`)).toBe(null);
  });

  it('should get an Observable with the value (object) specified under the key (value is cached)', (done) => {
    storageService['cacheCk$'][key] = new BehaviorSubject<any>(objectValue).asObservable();
    storageService.getCookie(key, true).subscribe(val => {
      expect(val).toEqual(objectValue);
      done();
    });
  });

  it('should get an Observable with the value (string) specified under the key (value is cached)', (done) => {
    storageService['cacheCk$'][key] = new BehaviorSubject<string>(stringValue).asObservable();
    storageService.getCookie(key).subscribe(val => {
      expect(val).toEqual(stringValue);
      done();
    });
  });

  it('should get an Observable with the value (object) specified under the key (value is NOT cached)', (done) => {
    storageService.getCookie(key, true).subscribe(val => {
      expect(val).toEqual(objectValue);
      done();
    });

    const args = cookieServiceSpy.getObject.calls.allArgs();
    expect(args[0][0]).toEqual(key);
  });

  it('should get an Observable with the value (string) specified under the key (value is NOT cached)', (done) => {
    storageService.getCookie(key).subscribe(val => {
      expect(val).toEqual(stringValue);
      done();
    });

    const args = cookieServiceSpy.get.calls.allArgs();
    expect(args[0][0]).toEqual(key);
  });

  it(`should get an Observable with the 'null' value when there is no specified value(object) under the key
    (and it is NOT cached)`, () => {
    const sampleValue = { mKey: `random ${ getRandomNumber() }MKey` };
    cookieServiceSpy.getObject.and.returnValue(sampleValue);
    storageService.getCookie(key, true).subscribe(val => expect(val).toEqual(sampleValue));

    const args = cookieServiceSpy.getObject.calls.allArgs();
    expect(args[0][0]).toEqual(key);
  });

  it('should clear the element saved under a key in both the cache and the cookies', (done) => {
    // mock values in cache
    storageService['cacheCk'][key] = new BehaviorSubject<string>(stringValue);
    storageService['cacheCk$'][key] = (storageService['cacheCk'][key] as BehaviorSubject<string>).asObservable();

    storageService.clearCookie(key).subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      expect((storageService['cacheCk'][key] as BehaviorSubject<string>).getValue()).toBeNull();
      done();
    });
    expect(cookieServiceSpy.remove).toHaveBeenCalled();
  });
  // endregion

  const valuePutterSetterChecker = (isReset: boolean, newVal: any) => {
    if (!isReset) {
      expect(newVal)
        .toEqual(objectValue, `cache not set for ${ key }: ${ JSON.stringify(objectValue) }`);
    } else {
      expect(newVal)
        .toEqual(stringValue, `cache not set for ${ key }: ${ stringValue }`);
    }
  };
});
