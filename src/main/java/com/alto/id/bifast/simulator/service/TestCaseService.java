package com.alto.id.bifast.simulator.service;

import com.alto.id.bifast.simulator.service.dto.TestCaseDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.alto.id.bifast.simulator.domain.TestCase}.
 */
public interface TestCaseService {
    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    TestCaseDTO save(TestCaseDTO testCaseDTO);

    /**
     * Updates a testCase.
     *
     * @param testCaseDTO the entity to update.
     * @return the persisted entity.
     */
    TestCaseDTO update(TestCaseDTO testCaseDTO);

    /**
     * Partially updates a testCase.
     *
     * @param testCaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestCaseDTO> partialUpdate(TestCaseDTO testCaseDTO);

    /**
     * Get all the testCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestCaseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" testCase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCaseDTO> findOne(Long id);

    /**
     * Delete the "id" testCase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
