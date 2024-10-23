import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITestCategory } from '../test-category.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../test-category.test-samples';

import { TestCategoryService } from './test-category.service';

const requireRestSample: ITestCategory = {
  ...sampleWithRequiredData,
};

describe('TestCategory Service', () => {
  let service: TestCategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ITestCategory | ITestCategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TestCategoryService);
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

    it('should create a TestCategory', () => {
      const testCategory = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(testCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TestCategory', () => {
      const testCategory = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(testCategory).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TestCategory', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TestCategory', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TestCategory', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTestCategoryToCollectionIfMissing', () => {
      it('should add a TestCategory to an empty array', () => {
        const testCategory: ITestCategory = sampleWithRequiredData;
        expectedResult = service.addTestCategoryToCollectionIfMissing([], testCategory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCategory);
      });

      it('should not add a TestCategory to an array that contains it', () => {
        const testCategory: ITestCategory = sampleWithRequiredData;
        const testCategoryCollection: ITestCategory[] = [
          {
            ...testCategory,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTestCategoryToCollectionIfMissing(testCategoryCollection, testCategory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TestCategory to an array that doesn't contain it", () => {
        const testCategory: ITestCategory = sampleWithRequiredData;
        const testCategoryCollection: ITestCategory[] = [sampleWithPartialData];
        expectedResult = service.addTestCategoryToCollectionIfMissing(testCategoryCollection, testCategory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCategory);
      });

      it('should add only unique TestCategory to an array', () => {
        const testCategoryArray: ITestCategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const testCategoryCollection: ITestCategory[] = [sampleWithRequiredData];
        expectedResult = service.addTestCategoryToCollectionIfMissing(testCategoryCollection, ...testCategoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const testCategory: ITestCategory = sampleWithRequiredData;
        const testCategory2: ITestCategory = sampleWithPartialData;
        expectedResult = service.addTestCategoryToCollectionIfMissing([], testCategory, testCategory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(testCategory);
        expect(expectedResult).toContain(testCategory2);
      });

      it('should accept null and undefined values', () => {
        const testCategory: ITestCategory = sampleWithRequiredData;
        expectedResult = service.addTestCategoryToCollectionIfMissing([], null, testCategory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(testCategory);
      });

      it('should return initial array if no TestCategory is added', () => {
        const testCategoryCollection: ITestCategory[] = [sampleWithRequiredData];
        expectedResult = service.addTestCategoryToCollectionIfMissing(testCategoryCollection, undefined, null);
        expect(expectedResult).toEqual(testCategoryCollection);
      });
    });

    describe('compareTestCategory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTestCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTestCategory(entity1, entity2);
        const compareResult2 = service.compareTestCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTestCategory(entity1, entity2);
        const compareResult2 = service.compareTestCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTestCategory(entity1, entity2);
        const compareResult2 = service.compareTestCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
