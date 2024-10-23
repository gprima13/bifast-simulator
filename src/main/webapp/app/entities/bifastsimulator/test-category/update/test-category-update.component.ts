import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestCategory } from '../test-category.model';
import { TestCategoryService } from '../service/test-category.service';
import { TestCategoryFormGroup, TestCategoryFormService } from './test-category-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-category-update',
  templateUrl: './test-category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestCategoryUpdateComponent implements OnInit {
  isSaving = false;
  testCategory: ITestCategory | null = null;

  protected testCategoryService = inject(TestCategoryService);
  protected testCategoryFormService = inject(TestCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestCategoryFormGroup = this.testCategoryFormService.createTestCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCategory }) => {
      this.testCategory = testCategory;
      if (testCategory) {
        this.updateForm(testCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCategory = this.testCategoryFormService.getTestCategory(this.editForm);
    if (testCategory.id !== null) {
      this.subscribeToSaveResponse(this.testCategoryService.update(testCategory));
    } else {
      this.subscribeToSaveResponse(this.testCategoryService.create(testCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCategory>>): void {
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

  protected updateForm(testCategory: ITestCategory): void {
    this.testCategory = testCategory;
    this.testCategoryFormService.resetForm(this.editForm, testCategory);
  }
}
