import { HttpParams } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import * as _ from 'lodash';

import { getRandomNumber } from '../../../shared/test-util/functions.util';
import { ParamsService } from './params.service';

describe('ParamsService', () => {
  let service: ParamsService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [ParamsService] });
    service = TestBed.get(ParamsService);
  });

  it('should be created', () => expect(service).toBeTruthy());

  it('#getHttpParams should and HttpParams object properly from the provided params', () => {
    const valueString = 'value1';
    const valueNumber = getRandomNumber();
    const valueObject = { sample: 'sample' };
    const valueBoolean = true;
    const valueArray = [valueString, valueNumber, valueObject, valueBoolean];
    const paramsObj: { [key: string]: any } = {
      key1: valueString,
      key2: valueNumber,
      key3: valueObject,
      key4: valueBoolean,
      key5: valueArray
    };
    const httpParams: HttpParams = service.getHttpParams(paramsObj);
    for (const k in paramsObj) {
      if (_.has(paramsObj, k)) {
        expect(httpParams.has(k)).toBeTruthy(`HttpParams doesn't have key: ${k}`);
        expect(paramsObj[k].toString()).toEqual(httpParams.get(k), `Params for key ${k} don't match`);
      }
    }
  });
});
