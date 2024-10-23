import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITestCase } from 'app/entities/bifastsimulator/test-case/test-case.model';
import { TestCaseService } from 'app/entities/bifastsimulator/test-case/service/test-case.service';
import { TestCaseDetailService } from '../service/test-case-detail.service';
import { ITestCaseDetail } from '../test-case-detail.model';
import { TestCaseDetailFormService } from './test-case-detail-form.service';

import { TestCaseDetailUpdateComponent } from './test-case-detail-update.component';

describe('TestCaseDetail Management Update Component', () => {
  let comp: TestCaseDetailUpdateComponent;
  let fixture: ComponentFixture<TestCaseDetailUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let testCaseDetailFormService: TestCaseDetailFormService;
  let testCaseDetailService: TestCaseDetailService;
  let testCaseService: TestCaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TestCaseDetailUpdateComponent],
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
      .overrideTemplate(TestCaseDetailUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TestCaseDetailUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    testCaseDetailFormService = TestBed.inject(TestCaseDetailFormService);
    testCaseDetailService = TestBed.inject(TestCaseDetailService);
    testCaseService = TestBed.inject(TestCaseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TestCase query and add missing value', () => {
      const testCaseDetail: ITestCaseDetail = { id: 456 };
      const testCase: ITestCase = { id: 1802 };
      testCaseDetail.testCase = testCase;

      const testCaseCollection: ITestCase[] = [{ id: 11084 }];
      jest.spyOn(testCaseService, 'query').mockReturnValue(of(new HttpResponse({ body: testCaseCollection })));
      const additionalTestCases = [testCase];
      const expectedCollection: ITestCase[] = [...additionalTestCases, ...testCaseCollection];
      jest.spyOn(testCaseService, 'addTestCaseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ testCaseDetail });
      comp.ngOnInit();

      expect(testCaseService.query).toHaveBeenCalled();
      expect(testCaseService.addTestCaseToCollectionIfMissing).toHaveBeenCalledWith(
        testCaseCollection,
        ...additionalTestCases.map(expect.objectContaining),
      );
      expect(comp.testCasesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const testCaseDetail: ITestCaseDetail = { id: 456 };
      const testCase: ITestCase = { id: 14618 };
      testCaseDetail.testCase = testCase;

      activatedRoute.data = of({ testCaseDetail });
      comp.ngOnInit();

      expect(comp.testCasesSharedCollection).toContain(testCase);
      expect(comp.testCaseDetail).toEqual(testCaseDetail);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCaseDetail>>();
      const testCaseDetail = { id: 123 };
      jest.spyOn(testCaseDetailFormService, 'getTestCaseDetail').mockReturnValue(testCaseDetail);
      jest.spyOn(testCaseDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCaseDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCaseDetail }));
      saveSubject.complete();

      // THEN
      expect(testCaseDetailFormService.getTestCaseDetail).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(testCaseDetailService.update).toHaveBeenCalledWith(expect.objectContaining(testCaseDetail));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCaseDetail>>();
      const testCaseDetail = { id: 123 };
      jest.spyOn(testCaseDetailFormService, 'getTestCaseDetail').mockReturnValue({ id: null });
      jest.spyOn(testCaseDetailService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCaseDetail: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: testCaseDetail }));
      saveSubject.complete();

      // THEN
      expect(testCaseDetailFormService.getTestCaseDetail).toHaveBeenCalled();
      expect(testCaseDetailService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITestCaseDetail>>();
      const testCaseDetail = { id: 123 };
      jest.spyOn(testCaseDetailService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ testCaseDetail });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(testCaseDetailService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTestCase', () => {
      it('Should forward to testCaseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(testCaseService, 'compareTestCase');
        comp.compareTestCase(entity, entity2);
        expect(testCaseService.compareTestCase).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
