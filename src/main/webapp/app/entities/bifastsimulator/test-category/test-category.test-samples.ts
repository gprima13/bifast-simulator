import { ITestCategory, NewTestCategory } from './test-category.model';

export const sampleWithRequiredData: ITestCategory = {
  id: 24515,
};

export const sampleWithPartialData: ITestCategory = {
  id: 12136,
  isActive: true,
};

export const sampleWithFullData: ITestCategory = {
  id: 17937,
  testSuiteName: 'dial gah',
  isActive: false,
};

export const sampleWithNewData: NewTestCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
