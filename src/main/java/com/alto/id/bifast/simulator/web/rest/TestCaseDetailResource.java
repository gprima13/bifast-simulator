package com.alto.id.bifast.simulator.web.rest;

import com.alto.id.bifast.simulator.repository.TestCaseDetailRepository;
import com.alto.id.bifast.simulator.service.TestCaseDetailService;
import com.alto.id.bifast.simulator.service.dto.TestCaseDetailDTO;
import com.alto.id.bifast.simulator.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.alto.id.bifast.simulator.domain.TestCaseDetail}.
 */
@RestController
@RequestMapping("/api/test-case-details")
public class TestCaseDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseDetailResource.class);

    private static final String ENTITY_NAME = "bifastsimulatorTestCaseDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseDetailService testCaseDetailService;

    private final TestCaseDetailRepository testCaseDetailRepository;

    public TestCaseDetailResource(TestCaseDetailService testCaseDetailService, TestCaseDetailRepository testCaseDetailRepository) {
        this.testCaseDetailService = testCaseDetailService;
        this.testCaseDetailRepository = testCaseDetailRepository;
    }

    /**
     * {@code POST  /test-case-details} : Create a new testCaseDetail.
     *
     * @param testCaseDetailDTO the testCaseDetailDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseDetailDTO, or with status {@code 400 (Bad Request)} if the testCaseDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCaseDetailDTO> createTestCaseDetail(@RequestBody TestCaseDetailDTO testCaseDetailDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TestCaseDetail : {}", testCaseDetailDTO);
        if (testCaseDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new testCaseDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testCaseDetailDTO = testCaseDetailService.save(testCaseDetailDTO);
        return ResponseEntity.created(new URI("/api/test-case-details/" + testCaseDetailDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testCaseDetailDTO.getId().toString()))
            .body(testCaseDetailDTO);
    }

    /**
     * {@code PUT  /test-case-details/:id} : Updates an existing testCaseDetail.
     *
     * @param id the id of the testCaseDetailDTO to save.
     * @param testCaseDetailDTO the testCaseDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDetailDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDetailDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseDetailDTO> updateTestCaseDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCaseDetailDTO testCaseDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TestCaseDetail : {}, {}", id, testCaseDetailDTO);
        if (testCaseDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testCaseDetailDTO = testCaseDetailService.update(testCaseDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCaseDetailDTO.getId().toString()))
            .body(testCaseDetailDTO);
    }

    /**
     * {@code PATCH  /test-case-details/:id} : Partial updates given fields of an existing testCaseDetail, field will ignore if it is null
     *
     * @param id the id of the testCaseDetailDTO to save.
     * @param testCaseDetailDTO the testCaseDetailDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDetailDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDetailDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseDetailDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDetailDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseDetailDTO> partialUpdateTestCaseDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCaseDetailDTO testCaseDetailDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TestCaseDetail partially : {}, {}", id, testCaseDetailDTO);
        if (testCaseDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDetailDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseDetailDTO> result = testCaseDetailService.partialUpdate(testCaseDetailDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCaseDetailDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-case-details} : get all the testCaseDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCaseDetails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TestCaseDetailDTO>> getAllTestCaseDetails(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TestCaseDetails");
        Page<TestCaseDetailDTO> page = testCaseDetailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /test-case-details/:id} : get the "id" testCaseDetail.
     *
     * @param id the id of the testCaseDetailDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseDetailDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseDetailDTO> getTestCaseDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TestCaseDetail : {}", id);
        Optional<TestCaseDetailDTO> testCaseDetailDTO = testCaseDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseDetailDTO);
    }

    /**
     * {@code DELETE  /test-case-details/:id} : delete the "id" testCaseDetail.
     *
     * @param id the id of the testCaseDetailDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCaseDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TestCaseDetail : {}", id);
        testCaseDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
