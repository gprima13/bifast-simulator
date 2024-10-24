package com.alto.id.bifast.simulator.web.rest;

import com.alto.id.bifast.simulator.repository.BankRepository;
import com.alto.id.bifast.simulator.service.BankService;
import com.alto.id.bifast.simulator.service.dto.BankDTO;
import com.alto.id.bifast.simulator.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.alto.id.bifast.simulator.domain.Bank}.
 */
@RestController
@RequestMapping("/api/banks")
public class BankResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bifastsimulatorBank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankService bankService;

    private final BankRepository bankRepository;

    public BankResource(BankService bankService, BankRepository bankRepository) {
        this.bankService = bankService;
        this.bankRepository = bankRepository;
    }

    /**
     * {@code POST  /banks} : Create a new bank.
     *
     * @param bankDTO the bankDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankDTO, or with status {@code 400 (Bad Request)} if the bank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BankDTO> createBank(@RequestBody BankDTO bankDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bank : {}", bankDTO);
        if (bankDTO.getId() != null) {
            throw new BadRequestAlertException("A new bank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bankDTO = bankService.save(bankDTO);
        return ResponseEntity.created(new URI("/api/banks/" + bankDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bankDTO.getId().toString()))
            .body(bankDTO);
    }

    /**
     * {@code PUT  /banks/:id} : Updates an existing bank.
     *
     * @param id the id of the bankDTO to save.
     * @param bankDTO the bankDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankDTO,
     * or with status {@code 400 (Bad Request)} if the bankDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankDTO> updateBank(@PathVariable(value = "id", required = false) final Long id, @RequestBody BankDTO bankDTO)
        throws URISyntaxException {
        LOG.debug("REST request to update Bank : {}, {}", id, bankDTO);
        if (bankDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bankDTO = bankService.update(bankDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankDTO.getId().toString()))
            .body(bankDTO);
    }

    /**
     * {@code PATCH  /banks/:id} : Partial updates given fields of an existing bank, field will ignore if it is null
     *
     * @param id the id of the bankDTO to save.
     * @param bankDTO the bankDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankDTO,
     * or with status {@code 400 (Bad Request)} if the bankDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankDTO> partialUpdateBank(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BankDTO bankDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bank partially : {}, {}", id, bankDTO);
        if (bankDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankDTO> result = bankService.partialUpdate(bankDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /banks} : get all the banks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of banks in body.
     */
    @GetMapping("")
    public List<BankDTO> getAllBanks() {
        LOG.debug("REST request to get all Banks");
        return bankService.findAll();
    }

    /**
     * {@code GET  /banks/:id} : get the "id" bank.
     *
     * @param id the id of the bankDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankDTO> getBank(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bank : {}", id);
        Optional<BankDTO> bankDTO = bankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankDTO);
    }

    /**
     * {@code DELETE  /banks/:id} : delete the "id" bank.
     *
     * @param id the id of the bankDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bank : {}", id);
        bankService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
