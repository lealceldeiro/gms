import { inject, TestBed } from '@angular/core/testing';

import { InterceptorHelperService } from './interceptor-helper.service';

describe('InterceptorHelperService', () => {
  const sampleUrl = 'test-url-with-random-values-9fj-df-3fdD-fdfFf3';
  let intHelpService: InterceptorHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InterceptorHelperService]
    });
    intHelpService = TestBed.get(InterceptorHelperService);
  });

  it('should be created', inject([InterceptorHelperService], (service: InterceptorHelperService) => {
    expect(service).toBeTruthy();
  }));

  it(`should (for 'isExcludedFromErrorHandling') return 'true' if the argument is inside the private var
    'excludedFromErrorHandling', 'false' otherwise`, () => {
    intHelpService['excludedFromErrorHandling'].push(sampleUrl);
    expect(intHelpService.isExcludedFromErrorHandling(sampleUrl)).toBeTruthy();
    expect(intHelpService.isExcludedFromErrorHandling('another-sample')).toBeFalsy();
  });

  it(`should (for 'addExcludedFromErrorHandling') add the argument inside the private var
    'excludedFromErrorHandling if the url was not already there (returns 'true', 'false' otherwise)`, () => {
    expect(intHelpService.addExcludedFromErrorHandling(sampleUrl)).toBeTruthy();
    expect(intHelpService['excludedFromErrorHandling']).toContain(sampleUrl);
    // url already there
    expect(intHelpService.addExcludedFromErrorHandling(sampleUrl)).toBeFalsy();
  });

  it(`should (for 'removeExcludedFromErrorHandling') remove the argument inside the private var
    'excludedFromErrorHandling'(returns 'true' if the args was already there, 'false' otherwise`, () => {
    const url = 'another-sample';
    intHelpService['excludedFromErrorHandling'].push(sampleUrl);
    // url is not in array
    expect(intHelpService['excludedFromErrorHandling']).not.toContain(url);
    expect(intHelpService.removeExcludedFromErrorHandling(url)).toBeFalsy();
    /// sampleUrl is in array
    expect(intHelpService['excludedFromErrorHandling']).toContain(sampleUrl);
    expect(intHelpService.removeExcludedFromErrorHandling(sampleUrl)).toBeTruthy();
  });
});
