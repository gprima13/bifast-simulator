import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITestCategory } from '../test-category.model';
import { TestCategoryService } from '../service/test-category.service';

const testCategoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ITestCategory> => {
  const id = route.params.id;
  if (id) {
    return inject(TestCategoryService)
      .find(id)
      .pipe(
        mergeMap((testCategory: HttpResponse<ITestCategory>) => {
          if (testCategory.body) {
            return of(testCategory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default testCategoryResolve;
