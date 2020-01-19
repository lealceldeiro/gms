import { inject, TestBed } from '@angular/core/testing';

import { PageNotFoundService } from './page-not-found.service';

describe('PageNotFoundService', () => {
  const url = '/test';
  let service: PageNotFoundService;
  let collection: Array<string> = [];

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PageNotFoundService]
    });
    service = TestBed.get(PageNotFoundService);
    collection = service['notFoundUrls'];
  });

  it('should be created', inject([PageNotFoundService], (srv: PageNotFoundService) => {
    expect(srv).toBeTruthy();
  }));

  it('#removeUrl should remove the url from the collection when it exist', () => {
    expect(collection).not.toContain(url, 'Collection had the value already');
    collection.push(url);
    expect(collection).toContain(url, 'Collection does not have the value');
    service.removeUrl(url);
    expect(collection).not.toContain(url, 'Collection did not remove the value');
  });

  it('#removeUrl should do nothing if the url is not in the collection', () => {
    expect(collection).not.toContain(url, 'Collection had the value already');
    service.removeUrl(url);
    expect(collection).not.toContain(url, 'Collection should not have the value');
  });

  it('#addUrl should add the url to the collection when the url is not in the collection already', () => {
    expect(collection).not.toContain(url, 'Collection had the value already');
    service.addUrl(url);
    expect(collection).toContain(url, 'Collection did not add the value');
  });

  it('#addUrl should do nothing when the url is in the collection already', () => {
    const collectionStringify = JSON.stringify(collection);
    expect(collection).not.toContain(url, 'Collection had the value already');
    collection.push(url);
    expect(collection).toContain(url, 'Collection did not add the value');
    service.addUrl(url);
    expect(collection.lastIndexOf(url))
      .toBe(0, `it added the value despite it was already in the collection. Collection is ${ collectionStringify }`);
  });

  it('#wasNotFound should return `true` if the url is in the collection', () => {
    expect(collection).not.toContain(url, 'Collection had the value already');
    collection.push(url);
    expect(service.wasNotFound(url)).toBeTruthy();
  });

  it('#wasNotFound should return `false` if the url is not in the collection', () => {
    expect(collection.indexOf(url)).toBe(-1, 'Collection had the value already');
    expect(service.wasNotFound(url)).toBeFalsy();
  });
});
