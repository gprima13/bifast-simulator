import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestCaseDetail } from '../test-case-detail.model';
import { TestCaseDetailService } from '../service/test-case-detail.service';

const testCaseDetailResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestCaseDetail> => {
  const id = route.params.id;
  if (id) {
    return inject(TestCaseDetailService)
      .find(id)
      .pipe(
        mergeMap((testCaseDetail: HttpResponse<ITestCaseDetail>) => {
          if (testCaseDetail.body) {
            return of(testCaseDetail.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default testCaseDetailResolve;
