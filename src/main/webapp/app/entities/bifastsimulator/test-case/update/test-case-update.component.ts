import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestCategory } from 'app/entities/bifastsimulator/test-category/test-category.model';
import { TestCategoryService } from 'app/entities/bifastsimulator/test-category/service/test-category.service';
import { ITestCase } from '../test-case.model';
import { TestCaseService } from '../service/test-case.service';
import { TestCaseFormGroup, TestCaseFormService } from './test-case-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-case-update',
  templateUrl: './test-case-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestCaseUpdateComponent implements OnInit {
  isSaving = false;
  testCase: ITestCase | null = null;

  testCategoriesSharedCollection: ITestCategory[] = [];

  protected testCaseService = inject(TestCaseService);
  protected testCaseFormService = inject(TestCaseFormService);
  protected testCategoryService = inject(TestCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestCaseFormGroup = this.testCaseFormService.createTestCaseFormGroup();

  compareTestCategory = (o1: ITestCategory | null, o2: ITestCategory | null): boolean =>
    this.testCategoryService.compareTestCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCase }) => {
      this.testCase = testCase;
      if (testCase) {
        this.updateForm(testCase);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCase = this.testCaseFormService.getTestCase(this.editForm);
    if (testCase.id !== null) {
      this.subscribeToSaveResponse(this.testCaseService.update(testCase));
    } else {
      this.subscribeToSaveResponse(this.testCaseService.create(testCase));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCase>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(testCase: ITestCase): void {
    this.testCase = testCase;
    this.testCaseFormService.resetForm(this.editForm, testCase);

    this.testCategoriesSharedCollection = this.testCategoryService.addTestCategoryToCollectionIfMissing<ITestCategory>(
      this.testCategoriesSharedCollection,
      testCase.category,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.testCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITestCategory[]>) => res.body ?? []))
      .pipe(
        map((testCategories: ITestCategory[]) =>
          this.testCategoryService.addTestCategoryToCollectionIfMissing<ITestCategory>(testCategories, this.testCase?.category),
        ),
      )
      .subscribe((testCategories: ITestCategory[]) => (this.testCategoriesSharedCollection = testCategories));
  }
}
