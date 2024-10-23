export interface IBank {
  id: number;
  bicCode?: string | null;
  bankName?: string | null;
}

export type NewBank = Omit<IBank, 'id'> & { id: null };
