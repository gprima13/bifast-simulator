import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TestCaseDetailDetailComponent } from './test-case-detail-detail.component';

describe('TestCaseDetail Management Detail Component', () => {
  let comp: TestCaseDetailDetailComponent;
  let fixture: ComponentFixture<TestCaseDetailDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestCaseDetailDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./test-case-detail-detail.component').then(m => m.TestCaseDetailDetailComponent),
              resolve: { testCaseDetail: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TestCaseDetailDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestCaseDetailDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load testCaseDetail on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TestCaseDetailDetailComponent);

      // THEN
      expect(instance.testCaseDetail()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
