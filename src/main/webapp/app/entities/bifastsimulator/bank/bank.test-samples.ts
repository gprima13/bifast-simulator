import { IBank, NewBank } from './bank.model';

export const sampleWithRequiredData: IBank = {
  id: 23232,
};

export const sampleWithPartialData: IBank = {
  id: 21335,
  bankName: 'overload afore',
};

export const sampleWithFullData: IBank = {
  id: 8694,
  bicCode: 'warming tangible incandescence',
  bankName: 'down because',
};

export const sampleWithNewData: NewBank = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
