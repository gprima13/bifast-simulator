import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITestCaseDetail, NewTestCaseDetail } from '../test-case-detail.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITestCaseDetail for edit and NewTestCaseDetailFormGroupInput for create.
 */
type TestCaseDetailFormGroupInput = ITestCaseDetail | PartialWithRequiredKeyOf<NewTestCaseDetail>;

type TestCaseDetailFormDefaults = Pick<NewTestCaseDetail, 'id' | 'isActive'>;

type TestCaseDetailFormGroupContent = {
  id: FormControl<ITestCaseDetail['id'] | NewTestCaseDetail['id']>;
  matchingParam: FormControl<ITestCaseDetail['matchingParam']>;
  matchingValue: FormControl<ITestCaseDetail['matchingValue']>;
  valueType: FormControl<ITestCaseDetail['valueType']>;
  isActive: FormControl<ITestCaseDetail['isActive']>;
  testCase: FormControl<ITestCaseDetail['testCase']>;
};

export type TestCaseDetailFormGroup = FormGroup<TestCaseDetailFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TestCaseDetailFormService {
  createTestCaseDetailFormGroup(testCaseDetail: TestCaseDetailFormGroupInput = { id: null }): TestCaseDetailFormGroup {
    const testCaseDetailRawValue = {
      ...this.getFormDefaults(),
      ...testCaseDetail,
    };
    return new FormGroup<TestCaseDetailFormGroupContent>({
      id: new FormControl(
        { value: testCaseDetailRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matchingParam: new FormControl(testCaseDetailRawValue.matchingParam),
      matchingValue: new FormControl(testCaseDetailRawValue.matchingValue),
      valueType: new FormControl(testCaseDetailRawValue.valueType),
      isActive: new FormControl(testCaseDetailRawValue.isActive),
      testCase: new FormControl(testCaseDetailRawValue.testCase),
    });
  }

  getTestCaseDetail(form: TestCaseDetailFormGroup): ITestCaseDetail | NewTestCaseDetail {
    return form.getRawValue() as ITestCaseDetail | NewTestCaseDetail;
  }

  resetForm(form: TestCaseDetailFormGroup, testCaseDetail: TestCaseDetailFormGroupInput): void {
    const testCaseDetailRawValue = { ...this.getFormDefaults(), ...testCaseDetail };
    form.reset(
      {
        ...testCaseDetailRawValue,
        id: { value: testCaseDetailRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TestCaseDetailFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
