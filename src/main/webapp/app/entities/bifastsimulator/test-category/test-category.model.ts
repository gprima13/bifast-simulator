export interface ITestCategory {
  id: number;
  testSuiteName?: string | null;
  isActive?: boolean | null;
}

export type NewTestCategory = Omit<ITestCategory, 'id'> & { id: null };
