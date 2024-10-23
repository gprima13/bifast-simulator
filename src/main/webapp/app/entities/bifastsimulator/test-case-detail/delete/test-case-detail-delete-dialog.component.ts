import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestCaseDetail } from '../test-case-detail.model';
import { TestCaseDetailService } from '../service/test-case-detail.service';

@Component({
  standalone: true,
  templateUrl: './test-case-detail-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestCaseDetailDeleteDialogComponent {
  testCaseDetail?: ITestCaseDetail;

  protected testCaseDetailService = inject(TestCaseDetailService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCaseDetailService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
