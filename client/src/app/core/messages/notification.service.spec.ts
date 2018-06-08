import { inject, TestBed } from '@angular/core/testing';
import { ToastrService } from 'ngx-toastr';

import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  const from3rdParty = { error: (a, b): any => spy.error(a, b) };
  const spy = { error: (a, b): any => {} };
  let errorSpy: jasmine.Spy;
  let notificationService: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationService, { provide: ToastrService, useValue: from3rdParty }]
    });
    notificationService = TestBed.get(NotificationService);
    errorSpy = spyOn(spy, 'error');
  });

  it('should be created', inject([NotificationService], (service: NotificationService) => {
    expect(service).toBeTruthy();
  }));

  it('should call the notification component in watch', () => {
    notificationService.error('testM', 'testT');
    expect(errorSpy).toHaveBeenCalledTimes(1);
    expect(errorSpy.calls.first().args[0]).toBe('testM');
    expect(errorSpy.calls.first().args[1]).toBe('testT');
  });
});
