import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestCategory, NewTestCategory } from '../test-category.model';

export type PartialUpdateTestCategory = Partial<ITestCategory> & Pick<ITestCategory, 'id'>;

export type EntityResponseType = HttpResponse<ITestCategory>;
export type EntityArrayResponseType = HttpResponse<ITestCategory[]>;

@Injectable({ providedIn: 'root' })
export class TestCategoryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-categories', 'bifastsimulator');

  create(testCategory: NewTestCategory): Observable<EntityResponseType> {
    return this.http.post<ITestCategory>(this.resourceUrl, testCategory, { observe: 'response' });
  }

  update(testCategory: ITestCategory): Observable<EntityResponseType> {
    return this.http.put<ITestCategory>(`${this.resourceUrl}/${this.getTestCategoryIdentifier(testCategory)}`, testCategory, {
      observe: 'response',
    });
  }

  partialUpdate(testCategory: PartialUpdateTestCategory): Observable<EntityResponseType> {
    return this.http.patch<ITestCategory>(`${this.resourceUrl}/${this.getTestCategoryIdentifier(testCategory)}`, testCategory, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestCategoryIdentifier(testCategory: Pick<ITestCategory, 'id'>): number {
    return testCategory.id;
  }

  compareTestCategory(o1: Pick<ITestCategory, 'id'> | null, o2: Pick<ITestCategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestCategoryIdentifier(o1) === this.getTestCategoryIdentifier(o2) : o1 === o2;
  }

  addTestCategoryToCollectionIfMissing<Type extends Pick<ITestCategory, 'id'>>(
    testCategoryCollection: Type[],
    ...testCategoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testCategories: Type[] = testCategoriesToCheck.filter(isPresent);
    if (testCategories.length > 0) {
      const testCategoryCollectionIdentifiers = testCategoryCollection.map(testCategoryItem =>
        this.getTestCategoryIdentifier(testCategoryItem),
      );
      const testCategoriesToAdd = testCategories.filter(testCategoryItem => {
        const testCategoryIdentifier = this.getTestCategoryIdentifier(testCategoryItem);
        if (testCategoryCollectionIdentifiers.includes(testCategoryIdentifier)) {
          return false;
        }
        testCategoryCollectionIdentifiers.push(testCategoryIdentifier);
        return true;
      });
      return [...testCategoriesToAdd, ...testCategoryCollection];
    }
    return testCategoryCollection;
  }
}
