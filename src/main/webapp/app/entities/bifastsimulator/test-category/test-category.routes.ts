import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TestCategoryResolve from './route/test-category-routing-resolve.service';

const testCategoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/test-category.component').then(m => m.TestCategoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/test-category-detail.component').then(m => m.TestCategoryDetailComponent),
    resolve: {
      testCategory: TestCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/test-category-update.component').then(m => m.TestCategoryUpdateComponent),
    resolve: {
      testCategory: TestCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/test-category-update.component').then(m => m.TestCategoryUpdateComponent),
    resolve: {
      testCategory: TestCategoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default testCategoryRoute;
