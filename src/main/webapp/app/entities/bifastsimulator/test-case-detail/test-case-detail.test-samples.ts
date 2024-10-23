import { ITestCaseDetail, NewTestCaseDetail } from './test-case-detail.model';

export const sampleWithRequiredData: ITestCaseDetail = {
  id: 10312,
};

export const sampleWithPartialData: ITestCaseDetail = {
  id: 30626,
  matchingValue: 'wisely',
  isActive: false,
};

export const sampleWithFullData: ITestCaseDetail = {
  id: 17744,
  matchingParam: 'where ignorant',
  matchingValue: 'until',
  valueType: 'BOOLEAN',
  isActive: false,
};

export const sampleWithNewData: NewTestCaseDetail = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
