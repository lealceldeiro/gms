import { inject, TestBed } from '@angular/core/testing';

import { ToastrService } from 'ngx-toastr';

import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let toastrServiceSpy: jasmine.SpyObj<ToastrService>;
  let notificationService: NotificationService;

  beforeEach(() => {
    toastrServiceSpy = jasmine.createSpyObj('ToastrService', ['error']);

    TestBed.configureTestingModule({
      providers: [NotificationService, { provide: ToastrService, useValue: toastrServiceSpy }]
    });
    notificationService = TestBed.get(NotificationService);
  });

  it('should be created', inject([NotificationService], (service: NotificationService) => {
    expect(service).toBeTruthy();
  }));

  it('should call the notification component in watch', () => {
    notificationService.error('testM', 'testT');
    expect(toastrServiceSpy.error).toHaveBeenCalledTimes(1);
    expect(toastrServiceSpy.error.calls.first().args[0]).toBe('testM');
    expect(toastrServiceSpy.error.calls.first().args[1]).toBe('testT');
  });
});
