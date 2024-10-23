import { ITestCase } from 'app/entities/bifastsimulator/test-case/test-case.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface ITestCaseDetail {
  id: number;
  matchingParam?: string | null;
  matchingValue?: string | null;
  valueType?: keyof typeof ValueType | null;
  isActive?: boolean | null;
  testCase?: Pick<ITestCase, 'id'> | null;
}

export type NewTestCaseDetail = Omit<ITestCaseDetail, 'id'> & { id: null };
