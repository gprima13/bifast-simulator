import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITestCategory } from '../test-category.model';

@Component({
  standalone: true,
  selector: 'jhi-test-category-detail',
  templateUrl: './test-category-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TestCategoryDetailComponent {
  testCategory = input<ITestCategory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
