import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TestCategoryService } from '../service/test-category.service';
import { ITestCategory } from '../test-category.model';
import { TestCategoryFormService } from './test-category-form.service';

import { TestCategoryUpdateComponent } from './test-category-update.component';

describe('TestCategory Management Update Component', () => {
  let comp: TestCategoryUpdateComponent;
  let fixture: ComponentFixture<TestCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testCategoryFormService: TestCategoryFormService;
  let testCategoryService: TestCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TestCategoryUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TestCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testCategoryFormService = TestBed.inject(TestCategoryFormService);
    testCategoryService = TestBed.inject(TestCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const testCategory: ITestCategory = { id: 456 };

      activatedRoute.data = of({ testCategory });
      comp.ngOnInit();

      expect(comp.testCategory).toEqual(testCategory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCategory>>();
      const testCategory = { id: 123 };
      jest.spyOn(testCategoryFormService, 'getTestCategory').mockReturnValue(testCategory);
      jest.spyOn(testCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCategory }));
      saveSubject.complete();

      // THEN
      expect(testCategoryFormService.getTestCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testCategoryService.update).toHaveBeenCalledWith(expect.objectContaining(testCategory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCategory>>();
      const testCategory = { id: 123 };
      jest.spyOn(testCategoryFormService, 'getTestCategory').mockReturnValue({ id: null });
      jest.spyOn(testCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCategory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCategory }));
      saveSubject.complete();

      // THEN
      expect(testCategoryFormService.getTestCategory).toHaveBeenCalled();
      expect(testCategoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCategory>>();
      const testCategory = { id: 123 };
      jest.spyOn(testCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testCategoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
