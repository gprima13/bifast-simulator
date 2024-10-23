import { ITestCategory } from 'app/entities/bifastsimulator/test-category/test-category.model';

export interface ITestCase {
  id: number;
  caseName?: string | null;
  responseBody?: string | null;
  transactionStatus?: string | null;
  responseCode?: string | null;
  isActive?: boolean | null;
  category?: Pick<ITestCategory, 'id'> | null;
}

export type NewTestCase = Omit<ITestCase, 'id'> & { id: null };
