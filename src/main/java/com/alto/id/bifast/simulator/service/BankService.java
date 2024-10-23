package com.alto.id.bifast.simulator.service;

import com.alto.id.bifast.simulator.service.dto.BankDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.alto.id.bifast.simulator.domain.Bank}.
 */
public interface BankService {
    /**
     * Save a bank.
     *
     * @param bankDTO the entity to save.
     * @return the persisted entity.
     */
    BankDTO save(BankDTO bankDTO);

    /**
     * Updates a bank.
     *
     * @param bankDTO the entity to update.
     * @return the persisted entity.
     */
    BankDTO update(BankDTO bankDTO);

    /**
     * Partially updates a bank.
     *
     * @param bankDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BankDTO> partialUpdate(BankDTO bankDTO);

    /**
     * Get all the banks.
     *
     * @return the list of entities.
     */
    List<BankDTO> findAll();

    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankDTO> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
