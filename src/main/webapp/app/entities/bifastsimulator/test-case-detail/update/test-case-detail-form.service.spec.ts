import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../test-case-detail.test-samples';

import { TestCaseDetailFormService } from './test-case-detail-form.service';

describe('TestCaseDetail Form Service', () => {
  let service: TestCaseDetailFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCaseDetailFormService);
  });

  describe('Service methods', () => {
    describe('createTestCaseDetailFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestCaseDetailFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matchingParam: expect.any(Object),
            matchingValue: expect.any(Object),
            valueType: expect.any(Object),
            isActive: expect.any(Object),
            testCase: expect.any(Object),
          }),
        );
      });

      it('passing ITestCaseDetail should create a new form with FormGroup', () => {
        const formGroup = service.createTestCaseDetailFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matchingParam: expect.any(Object),
            matchingValue: expect.any(Object),
            valueType: expect.any(Object),
            isActive: expect.any(Object),
            testCase: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestCaseDetail', () => {
      it('should return NewTestCaseDetail for default TestCaseDetail initial value', () => {
        const formGroup = service.createTestCaseDetailFormGroup(sampleWithNewData);

        const testCaseDetail = service.getTestCaseDetail(formGroup) as any;

        expect(testCaseDetail).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestCaseDetail for empty TestCaseDetail initial value', () => {
        const formGroup = service.createTestCaseDetailFormGroup();

        const testCaseDetail = service.getTestCaseDetail(formGroup) as any;

        expect(testCaseDetail).toMatchObject({});
      });

      it('should return ITestCaseDetail', () => {
        const formGroup = service.createTestCaseDetailFormGroup(sampleWithRequiredData);

        const testCaseDetail = service.getTestCaseDetail(formGroup) as any;

        expect(testCaseDetail).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestCaseDetail should not enable id FormControl', () => {
        const formGroup = service.createTestCaseDetailFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestCaseDetail should disable id FormControl', () => {
        const formGroup = service.createTestCaseDetailFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
