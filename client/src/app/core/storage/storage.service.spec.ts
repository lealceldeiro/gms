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
  const spies = {
    ls: {
      set: function(a, b) {},
      get: function(a) {},
      clear: function() {},
      removeItem: function(a) {},
    }
  };
  function setItemFailed(a, b): Observable<boolean> {
    spies.ls.set(a, b);
    return Observable.create(observer => {
      observer.error(new Error('test error'));
      observer.complete();
    });
  }
  let setItemFn = function(a, b): Observable<boolean> { spies.ls.set(a, b); return boolObs$; };

  const cookiesMock = {};
  const localStorageMock = {
    setItem: function(): Observable<boolean> { return setItemFn(arguments[0], arguments[1]); },
    getItem: function(): Observable<any> { spies.ls.get(arguments[0]); return objectValue$; },
    clear: function(): Observable<any> { spies.ls.clear(); return boolObs$; },
    removeItem: function(): Observable<any> { spies.ls.removeItem(arguments[0]); return boolObs$; },
  };
  // endregion

  // region spies
  let setItemSpy: jasmine.Spy;
  let getItemSpy: jasmine.Spy;
  let clearItemSpy: jasmine.Spy;
  let removeItemSpy: jasmine.Spy;
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

    // spies
    setItemSpy = spyOn(spies.ls, 'set');
    getItemSpy = spyOn(spies.ls, 'get');
    clearItemSpy = spyOn(spies.ls, 'clear');
    removeItemSpy = spyOn(spies.ls, 'removeItem');
  });

  it('should be created', inject([StorageService], (service: StorageService) => {
    expect(service).toBeTruthy();
  }));

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

  it('should re-try to set the value 2 times more if the first time it fails', fakeAsync(() => {
    consoleWarnSpy = spyOn(console, 'warn');
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
    storageService.clear().subscribe((finished: boolean) => {
      expect(finished).toBeTruthy('operation did not finished properly');
      for (const k in storageService['cache']) {
        if (storageService['cache'].hasOwnProperty(k)) {
          expect((storageService['cache'] as BehaviorSubject<any>).getValue()).toBeNull();
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
});
