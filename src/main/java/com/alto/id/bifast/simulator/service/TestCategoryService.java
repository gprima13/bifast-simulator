package com.alto.id.bifast.simulator.service;

import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.alto.id.bifast.simulator.domain.TestCategory}.
 */
public interface TestCategoryService {
    /**
     * Save a testCategory.
     *
     * @param testCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    TestCategoryDTO save(TestCategoryDTO testCategoryDTO);

    /**
     * Updates a testCategory.
     *
     * @param testCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    TestCategoryDTO update(TestCategoryDTO testCategoryDTO);

    /**
     * Partially updates a testCategory.
     *
     * @param testCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestCategoryDTO> partialUpdate(TestCategoryDTO testCategoryDTO);

    /**
     * Get all the testCategories.
     *
     * @return the list of entities.
     */
    List<TestCategoryDTO> findAll();

    /**
     * Get the "id" testCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" testCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
