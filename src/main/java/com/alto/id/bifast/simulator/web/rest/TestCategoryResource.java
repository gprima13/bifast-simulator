package com.alto.id.bifast.simulator.web.rest;

import com.alto.id.bifast.simulator.repository.TestCategoryRepository;
import com.alto.id.bifast.simulator.service.TestCategoryService;
import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
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
 * REST controller for managing {@link com.alto.id.bifast.simulator.domain.TestCategory}.
 */
@RestController
@RequestMapping("/api/test-categories")
public class TestCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestCategoryResource.class);

    private static final String ENTITY_NAME = "bifastsimulatorTestCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCategoryService testCategoryService;

    private final TestCategoryRepository testCategoryRepository;

    public TestCategoryResource(TestCategoryService testCategoryService, TestCategoryRepository testCategoryRepository) {
        this.testCategoryService = testCategoryService;
        this.testCategoryRepository = testCategoryRepository;
    }

    /**
     * {@code POST  /test-categories} : Create a new testCategory.
     *
     * @param testCategoryDTO the testCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCategoryDTO, or with status {@code 400 (Bad Request)} if the testCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCategoryDTO> createTestCategory(@RequestBody TestCategoryDTO testCategoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save TestCategory : {}", testCategoryDTO);
        if (testCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new testCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testCategoryDTO = testCategoryService.save(testCategoryDTO);
        return ResponseEntity.created(new URI("/api/test-categories/" + testCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testCategoryDTO.getId().toString()))
            .body(testCategoryDTO);
    }

    /**
     * {@code PUT  /test-categories/:id} : Updates an existing testCategory.
     *
     * @param id the id of the testCategoryDTO to save.
     * @param testCategoryDTO the testCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the testCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCategoryDTO> updateTestCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCategoryDTO testCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TestCategory : {}, {}", id, testCategoryDTO);
        if (testCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testCategoryDTO = testCategoryService.update(testCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCategoryDTO.getId().toString()))
            .body(testCategoryDTO);
    }

    /**
     * {@code PATCH  /test-categories/:id} : Partial updates given fields of an existing testCategory, field will ignore if it is null
     *
     * @param id the id of the testCategoryDTO to save.
     * @param testCategoryDTO the testCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the testCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCategoryDTO> partialUpdateTestCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCategoryDTO testCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TestCategory partially : {}, {}", id, testCategoryDTO);
        if (testCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCategoryDTO> result = testCategoryService.partialUpdate(testCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-categories} : get all the testCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCategories in body.
     */
    @GetMapping("")
    public List<TestCategoryDTO> getAllTestCategories() {
        LOG.debug("REST request to get all TestCategories");
        return testCategoryService.findAll();
    }

    /**
     * {@code GET  /test-categories/:id} : get the "id" testCategory.
     *
     * @param id the id of the testCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCategoryDTO> getTestCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TestCategory : {}", id);
        Optional<TestCategoryDTO> testCategoryDTO = testCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCategoryDTO);
    }

    /**
     * {@code DELETE  /test-categories/:id} : delete the "id" testCategory.
     *
     * @param id the id of the testCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TestCategory : {}", id);
        testCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
