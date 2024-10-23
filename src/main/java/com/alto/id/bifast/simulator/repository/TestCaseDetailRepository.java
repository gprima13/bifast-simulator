package com.alto.id.bifast.simulator.repository;

import com.alto.id.bifast.simulator.domain.TestCaseDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCaseDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseDetailRepository extends JpaRepository<TestCaseDetail, Long> {}
