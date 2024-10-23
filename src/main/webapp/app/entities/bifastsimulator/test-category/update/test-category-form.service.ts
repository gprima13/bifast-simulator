import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITestCategory, NewTestCategory } from '../test-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestCategory for edit and NewTestCategoryFormGroupInput for create.
 */
type TestCategoryFormGroupInput = ITestCategory | PartialWithRequiredKeyOf<NewTestCategory>;

type TestCategoryFormDefaults = Pick<NewTestCategory, 'id' | 'isActive'>;

type TestCategoryFormGroupContent = {
  id: FormControl<ITestCategory['id'] | NewTestCategory['id']>;
  testSuiteName: FormControl<ITestCategory['testSuiteName']>;
  isActive: FormControl<ITestCategory['isActive']>;
};

export type TestCategoryFormGroup = FormGroup<TestCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestCategoryFormService {
  createTestCategoryFormGroup(testCategory: TestCategoryFormGroupInput = { id: null }): TestCategoryFormGroup {
    const testCategoryRawValue = {
      ...this.getFormDefaults(),
      ...testCategory,
    };
    return new FormGroup<TestCategoryFormGroupContent>({
      id: new FormControl(
        { value: testCategoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      testSuiteName: new FormControl(testCategoryRawValue.testSuiteName),
      isActive: new FormControl(testCategoryRawValue.isActive),
    });
  }

  getTestCategory(form: TestCategoryFormGroup): ITestCategory | NewTestCategory {
    return form.getRawValue() as ITestCategory | NewTestCategory;
  }

  resetForm(form: TestCategoryFormGroup, testCategory: TestCategoryFormGroupInput): void {
    const testCategoryRawValue = { ...this.getFormDefaults(), ...testCategory };
    form.reset(
      {
        ...testCategoryRawValue,
        id: { value: testCategoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestCategoryFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
