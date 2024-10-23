package com.alto.id.bifast.simulator.repository;

import com.alto.id.bifast.simulator.domain.TestCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCategoryRepository extends JpaRepository<TestCategory, Long> {}
