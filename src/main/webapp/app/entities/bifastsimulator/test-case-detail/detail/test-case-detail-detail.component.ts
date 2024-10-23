import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITestCaseDetail } from '../test-case-detail.model';

@Component({
  standalone: true,
  selector: 'jhi-test-case-detail-detail',
  templateUrl: './test-case-detail-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TestCaseDetailDetailComponent {
  testCaseDetail = input<ITestCaseDetail | null>(null);

  previousState(): void {
    window.history.back();
  }
}
