import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITestCase } from 'app/entities/bifastsimulator/test-case/test-case.model';
import { TestCaseService } from 'app/entities/bifastsimulator/test-case/service/test-case.service';
import { ValueType } from 'app/entities/enumerations/value-type.model';
import { TestCaseDetailService } from '../service/test-case-detail.service';
import { ITestCaseDetail } from '../test-case-detail.model';
import { TestCaseDetailFormGroup, TestCaseDetailFormService } from './test-case-detail-form.service';

@Component({
  standalone: true,
  selector: 'jhi-test-case-detail-update',
  templateUrl: './test-case-detail-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TestCaseDetailUpdateComponent implements OnInit {
  isSaving = false;
  testCaseDetail: ITestCaseDetail | null = null;
  valueTypeValues = Object.keys(ValueType);

  testCasesSharedCollection: ITestCase[] = [];

  protected testCaseDetailService = inject(TestCaseDetailService);
  protected testCaseDetailFormService = inject(TestCaseDetailFormService);
  protected testCaseService = inject(TestCaseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TestCaseDetailFormGroup = this.testCaseDetailFormService.createTestCaseDetailFormGroup();

  compareTestCase = (o1: ITestCase | null, o2: ITestCase | null): boolean => this.testCaseService.compareTestCase(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ testCaseDetail }) => {
      this.testCaseDetail = testCaseDetail;
      if (testCaseDetail) {
        this.updateForm(testCaseDetail);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const testCaseDetail = this.testCaseDetailFormService.getTestCaseDetail(this.editForm);
    if (testCaseDetail.id !== null) {
      this.subscribeToSaveResponse(this.testCaseDetailService.update(testCaseDetail));
    } else {
      this.subscribeToSaveResponse(this.testCaseDetailService.create(testCaseDetail));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITestCaseDetail>>): void {
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

  protected updateForm(testCaseDetail: ITestCaseDetail): void {
    this.testCaseDetail = testCaseDetail;
    this.testCaseDetailFormService.resetForm(this.editForm, testCaseDetail);

    this.testCasesSharedCollection = this.testCaseService.addTestCaseToCollectionIfMissing<ITestCase>(
      this.testCasesSharedCollection,
      testCaseDetail.testCase,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.testCaseService
      .query()
      .pipe(map((res: HttpResponse<ITestCase[]>) => res.body ?? []))
      .pipe(
        map((testCases: ITestCase[]) =>
          this.testCaseService.addTestCaseToCollectionIfMissing<ITestCase>(testCases, this.testCaseDetail?.testCase),
        ),
      )
      .subscribe((testCases: ITestCase[]) => (this.testCasesSharedCollection = testCases));
  }
}
