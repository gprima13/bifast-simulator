import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITestCaseDetail, NewTestCaseDetail } from '../test-case-detail.model';

export type PartialUpdateTestCaseDetail = Partial<ITestCaseDetail> & Pick<ITestCaseDetail, 'id'>;

export type EntityResponseType = HttpResponse<ITestCaseDetail>;
export type EntityArrayResponseType = HttpResponse<ITestCaseDetail[]>;

@Injectable({ providedIn: 'root' })
export class TestCaseDetailService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/test-case-details', 'bifastsimulator');

  create(testCaseDetail: NewTestCaseDetail): Observable<EntityResponseType> {
    return this.http.post<ITestCaseDetail>(this.resourceUrl, testCaseDetail, { observe: 'response' });
  }

  update(testCaseDetail: ITestCaseDetail): Observable<EntityResponseType> {
    return this.http.put<ITestCaseDetail>(`${this.resourceUrl}/${this.getTestCaseDetailIdentifier(testCaseDetail)}`, testCaseDetail, {
      observe: 'response',
    });
  }

  partialUpdate(testCaseDetail: PartialUpdateTestCaseDetail): Observable<EntityResponseType> {
    return this.http.patch<ITestCaseDetail>(`${this.resourceUrl}/${this.getTestCaseDetailIdentifier(testCaseDetail)}`, testCaseDetail, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITestCaseDetail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITestCaseDetail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTestCaseDetailIdentifier(testCaseDetail: Pick<ITestCaseDetail, 'id'>): number {
    return testCaseDetail.id;
  }

  compareTestCaseDetail(o1: Pick<ITestCaseDetail, 'id'> | null, o2: Pick<ITestCaseDetail, 'id'> | null): boolean {
    return o1 && o2 ? this.getTestCaseDetailIdentifier(o1) === this.getTestCaseDetailIdentifier(o2) : o1 === o2;
  }

  addTestCaseDetailToCollectionIfMissing<Type extends Pick<ITestCaseDetail, 'id'>>(
    testCaseDetailCollection: Type[],
    ...testCaseDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const testCaseDetails: Type[] = testCaseDetailsToCheck.filter(isPresent);
    if (testCaseDetails.length > 0) {
      const testCaseDetailCollectionIdentifiers = testCaseDetailCollection.map(testCaseDetailItem =>
        this.getTestCaseDetailIdentifier(testCaseDetailItem),
      );
      const testCaseDetailsToAdd = testCaseDetails.filter(testCaseDetailItem => {
        const testCaseDetailIdentifier = this.getTestCaseDetailIdentifier(testCaseDetailItem);
        if (testCaseDetailCollectionIdentifiers.includes(testCaseDetailIdentifier)) {
          return false;
        }
        testCaseDetailCollectionIdentifiers.push(testCaseDetailIdentifier);
        return true;
      });
      return [...testCaseDetailsToAdd, ...testCaseDetailCollection];
    }
    return testCaseDetailCollection;
  }
}
