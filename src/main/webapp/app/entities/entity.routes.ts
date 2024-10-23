import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'test-category',
    data: { pageTitle: 'bifastsimulatorApp.bifastsimulatorTestCategory.home.title' },
    loadChildren: () => import('./bifastsimulator/test-category/test-category.routes'),
  },
  {
    path: 'test-case',
    data: { pageTitle: 'bifastsimulatorApp.bifastsimulatorTestCase.home.title' },
    loadChildren: () => import('./bifastsimulator/test-case/test-case.routes'),
  },
  {
    path: 'test-case-detail',
    data: { pageTitle: 'bifastsimulatorApp.bifastsimulatorTestCaseDetail.home.title' },
    loadChildren: () => import('./bifastsimulator/test-case-detail/test-case-detail.routes'),
  },
  {
    path: 'bank',
    data: { pageTitle: 'bifastsimulatorApp.bifastsimulatorBank.home.title' },
    loadChildren: () => import('./bifastsimulator/bank/bank.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
