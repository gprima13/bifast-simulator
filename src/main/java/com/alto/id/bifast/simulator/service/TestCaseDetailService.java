package com.alto.id.bifast.simulator.service;

import com.alto.id.bifast.simulator.service.dto.TestCaseDetailDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.alto.id.bifast.simulator.domain.TestCaseDetail}.
 */
public interface TestCaseDetailService {
    /**
     * Save a testCaseDetail.
     *
     * @param testCaseDetailDTO the entity to save.
     * @return the persisted entity.
     */
    TestCaseDetailDTO save(TestCaseDetailDTO testCaseDetailDTO);

    /**
     * Updates a testCaseDetail.
     *
     * @param testCaseDetailDTO the entity to update.
     * @return the persisted entity.
     */
    TestCaseDetailDTO update(TestCaseDetailDTO testCaseDetailDTO);

    /**
     * Partially updates a testCaseDetail.
     *
     * @param testCaseDetailDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestCaseDetailDTO> partialUpdate(TestCaseDetailDTO testCaseDetailDTO);

    /**
     * Get all the testCaseDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestCaseDetailDTO> findAll(Pageable pageable);

    /**
     * Get the "id" testCaseDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCaseDetailDTO> findOne(Long id);

    /**
     * Delete the "id" testCaseDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
