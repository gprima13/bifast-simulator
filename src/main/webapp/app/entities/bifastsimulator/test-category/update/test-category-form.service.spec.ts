import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../test-category.test-samples';

import { TestCategoryFormService } from './test-category-form.service';

describe('TestCategory Form Service', () => {
  let service: TestCategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TestCategoryFormService);
  });

  describe('Service methods', () => {
    describe('createTestCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTestCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testSuiteName: expect.any(Object),
            isActive: expect.any(Object),
          }),
        );
      });

      it('passing ITestCategory should create a new form with FormGroup', () => {
        const formGroup = service.createTestCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            testSuiteName: expect.any(Object),
            isActive: expect.any(Object),
          }),
        );
      });
    });

    describe('getTestCategory', () => {
      it('should return NewTestCategory for default TestCategory initial value', () => {
        const formGroup = service.createTestCategoryFormGroup(sampleWithNewData);

        const testCategory = service.getTestCategory(formGroup) as any;

        expect(testCategory).toMatchObject(sampleWithNewData);
      });

      it('should return NewTestCategory for empty TestCategory initial value', () => {
        const formGroup = service.createTestCategoryFormGroup();

        const testCategory = service.getTestCategory(formGroup) as any;

        expect(testCategory).toMatchObject({});
      });

      it('should return ITestCategory', () => {
        const formGroup = service.createTestCategoryFormGroup(sampleWithRequiredData);

        const testCategory = service.getTestCategory(formGroup) as any;

        expect(testCategory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITestCategory should not enable id FormControl', () => {
        const formGroup = service.createTestCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTestCategory should disable id FormControl', () => {
        const formGroup = service.createTestCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
