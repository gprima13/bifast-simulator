import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITestCategory } from '../test-category.model';
import { TestCategoryService } from '../service/test-category.service';

@Component({
  standalone: true,
  templateUrl: './test-category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TestCategoryDeleteDialogComponent {
  testCategory?: ITestCategory;

  protected testCategoryService = inject(TestCategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.testCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
