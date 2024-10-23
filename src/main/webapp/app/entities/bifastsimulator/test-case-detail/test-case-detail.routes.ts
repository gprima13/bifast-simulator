import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TestCaseDetailResolve from './route/test-case-detail-routing-resolve.service';

const testCaseDetailRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/test-case-detail.component').then(m => m.TestCaseDetailComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/test-case-detail-detail.component').then(m => m.TestCaseDetailDetailComponent),
    resolve: {
      testCaseDetail: TestCaseDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/test-case-detail-update.component').then(m => m.TestCaseDetailUpdateComponent),
    resolve: {
      testCaseDetail: TestCaseDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/test-case-detail-update.component').then(m => m.TestCaseDetailUpdateComponent),
    resolve: {
      testCaseDetail: TestCaseDetailResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testCaseDetailRoute;
