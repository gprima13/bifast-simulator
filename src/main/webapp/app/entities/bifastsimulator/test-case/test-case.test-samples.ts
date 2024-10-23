import { ITestCase, NewTestCase } from './test-case.model';

export const sampleWithRequiredData: ITestCase = {
  id: 29842,
};

export const sampleWithPartialData: ITestCase = {
  id: 14291,
  caseName: 'kiddingly a',
  transactionStatus: 'duh',
};

export const sampleWithFullData: ITestCase = {
  id: 29832,
  caseName: 'supposing infatuated uncork',
  responseBody: 'eventually quick',
  transactionStatus: 'scarcely hastily',
  responseCode: 'shoulder',
  isActive: true,
};

export const sampleWithNewData: NewTestCase = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
