import { inject, TestBed } from '@angular/core/testing';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormHelperService } from './form-helper.service';

describe('HelperService', () => {
  let formHelperService: FormHelperService;
  let fb: FormBuilder;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, FormsModule],
      providers: [FormHelperService]
    });
    fb = TestBed.get(FormBuilder);
    formHelperService = TestBed.get(FormHelperService);
  });

  it('should be created', inject([FormHelperService], (service: FormHelperService) => {
    expect(service).toBeTruthy();
  }));

  it('should mark every control inside a form as touched', () => {
    const fg = fb.group({
      'c1': 'someValue',
      'c2': 'someOtherVal'
    });
    let c1 = fg.get('c1');
    expect(c1).not.toBeNull();
    if (c1) {
      expect(c1.touched).toBeFalsy();
    }
    let c2 = fg.get('c2');
    expect(c2).not.toBeNull();
    if (c2) {
      expect(c2.touched).toBeFalsy();
    }
    formHelperService.markFormElementsAsTouched(fg);
    c1 = fg.get('c1');
    expect(c1).not.toBeNull();
    if (c1) {
      expect(c1.touched).toBeTruthy();
    }
    c2 = fg.get('c2');
    expect(c2).not.toBeNull();
    if (c2) {
      expect(c2.touched).toBeTruthy();
    }
  });
});
