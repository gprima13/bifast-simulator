import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITestCaseDetail } from '../test-case-detail.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../test-case-detail.test-samples';

import { TestCaseDetailService } from './test-case-detail.service';

const requireRestSample: ITestCaseDetail = {
  ...sampleWithRequiredData,
};

describe('TestCaseDetail Service', () => {
  let service: TestCaseDetailService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestCaseDetail | ITestCaseDetail[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TestCaseDetailService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TestCaseDetail', () => {
      const testCaseDetail = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testCaseDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestCaseDetail', () => {
      const testCaseDetail = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testCaseDetail).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestCaseDetail', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestCaseDetail', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestCaseDetail', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestCaseDetailToCollectionIfMissing', () => {
      it('should add a TestCaseDetail to an empty array', () => {
        const testCaseDetail: ITestCaseDetail = sampleWithRequiredData;
        expectedResult = service.addTestCaseDetailToCollectionIfMissing([], testCaseDetail);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCaseDetail);
      });

      it('should not add a TestCaseDetail to an array that contains it', () => {
        const testCaseDetail: ITestCaseDetail = sampleWithRequiredData;
        const testCaseDetailCollection: ITestCaseDetail[] = [
          {
            ...testCaseDetail,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestCaseDetailToCollectionIfMissing(testCaseDetailCollection, testCaseDetail);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestCaseDetail to an array that doesn't contain it", () => {
        const testCaseDetail: ITestCaseDetail = sampleWithRequiredData;
        const testCaseDetailCollection: ITestCaseDetail[] = [sampleWithPartialData];
        expectedResult = service.addTestCaseDetailToCollectionIfMissing(testCaseDetailCollection, testCaseDetail);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCaseDetail);
      });

      it('should add only unique TestCaseDetail to an array', () => {
        const testCaseDetailArray: ITestCaseDetail[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testCaseDetailCollection: ITestCaseDetail[] = [sampleWithRequiredData];
        expectedResult = service.addTestCaseDetailToCollectionIfMissing(testCaseDetailCollection, ...testCaseDetailArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testCaseDetail: ITestCaseDetail = sampleWithRequiredData;
        const testCaseDetail2: ITestCaseDetail = sampleWithPartialData;
        expectedResult = service.addTestCaseDetailToCollectionIfMissing([], testCaseDetail, testCaseDetail2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCaseDetail);
        expect(expectedResult).toContain(testCaseDetail2);
      });

      it('should accept null and undefined values', () => {
        const testCaseDetail: ITestCaseDetail = sampleWithRequiredData;
        expectedResult = service.addTestCaseDetailToCollectionIfMissing([], null, testCaseDetail, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCaseDetail);
      });

      it('should return initial array if no TestCaseDetail is added', () => {
        const testCaseDetailCollection: ITestCaseDetail[] = [sampleWithRequiredData];
        expectedResult = service.addTestCaseDetailToCollectionIfMissing(testCaseDetailCollection, undefined, null);
        expect(expectedResult).toEqual(testCaseDetailCollection);
      });
    });

    describe('compareTestCaseDetail', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestCaseDetail(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestCaseDetail(entity1, entity2);
        const compareResult2 = service.compareTestCaseDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestCaseDetail(entity1, entity2);
        const compareResult2 = service.compareTestCaseDetail(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestCaseDetail(entity1, entity2);
        const compareResult2 = service.compareTestCaseDetail(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
