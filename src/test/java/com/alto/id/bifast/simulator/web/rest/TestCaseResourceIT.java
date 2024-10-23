package com.alto.id.bifast.simulator.web.rest;

import static com.alto.id.bifast.simulator.domain.TestCaseAsserts.*;
import static com.alto.id.bifast.simulator.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alto.id.bifast.simulator.IntegrationTest;
import com.alto.id.bifast.simulator.domain.TestCase;
import com.alto.id.bifast.simulator.repository.TestCaseRepository;
import com.alto.id.bifast.simulator.service.dto.TestCaseDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCaseMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestCaseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseResourceIT {

    private static final String DEFAULT_CASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CASE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/test-cases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseMockMvc;

    private TestCase testCase;

    private TestCase insertedTestCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createEntity() {
        return new TestCase()
            .caseName(DEFAULT_CASE_NAME)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .transactionStatus(DEFAULT_TRANSACTION_STATUS)
            .responseCode(DEFAULT_RESPONSE_CODE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCase createUpdatedEntity() {
        return new TestCase()
            .caseName(UPDATED_CASE_NAME)
            .responseBody(UPDATED_RESPONSE_BODY)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        testCase = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTestCase != null) {
            testCaseRepository.delete(insertedTestCase);
            insertedTestCase = null;
        }
    }

    @Test
    @Transactional
    void createTestCase() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);
        var returnedTestCaseDTO = om.readValue(
            restTestCaseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestCaseDTO.class
        );

        // Validate the TestCase in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTestCase = testCaseMapper.toEntity(returnedTestCaseDTO);
        assertTestCaseUpdatableFieldsEquals(returnedTestCase, getPersistedTestCase(returnedTestCase));

        insertedTestCase = returnedTestCase;
    }

    @Test
    @Transactional
    void createTestCaseWithExistingId() throws Exception {
        // Create the TestCase with an existing ID
        testCase.setId(1L);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCases() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        // Get all the testCaseList
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].caseName").value(hasItem(DEFAULT_CASE_NAME)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS)))
            .andExpect(jsonPath("$.[*].responseCode").value(hasItem(DEFAULT_RESPONSE_CODE)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getTestCase() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        // Get the testCase
        restTestCaseMockMvc
            .perform(get(ENTITY_API_URL_ID, testCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCase.getId().intValue()))
            .andExpect(jsonPath("$.caseName").value(DEFAULT_CASE_NAME))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY))
            .andExpect(jsonPath("$.transactionStatus").value(DEFAULT_TRANSACTION_STATUS))
            .andExpect(jsonPath("$.responseCode").value(DEFAULT_RESPONSE_CODE))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestCase() throws Exception {
        // Get the testCase
        restTestCaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCase() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCase
        TestCase updatedTestCase = testCaseRepository.findById(testCase.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCase are not directly saved in db
        em.detach(updatedTestCase);
        updatedTestCase
            .caseName(UPDATED_CASE_NAME)
            .responseBody(UPDATED_RESPONSE_BODY)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .isActive(UPDATED_IS_ACTIVE);
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(updatedTestCase);

        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestCaseToMatchAllProperties(updatedTestCase);
    }

    @Test
    @Transactional
    void putNonExistingTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase.isActive(UPDATED_IS_ACTIVE);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCaseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTestCase, testCase), getPersistedTestCase(testCase));
    }

    @Test
    @Transactional
    void fullUpdateTestCaseWithPatch() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCase using partial update
        TestCase partialUpdatedTestCase = new TestCase();
        partialUpdatedTestCase.setId(testCase.getId());

        partialUpdatedTestCase
            .caseName(UPDATED_CASE_NAME)
            .responseBody(UPDATED_RESPONSE_BODY)
            .transactionStatus(UPDATED_TRANSACTION_STATUS)
            .responseCode(UPDATED_RESPONSE_CODE)
            .isActive(UPDATED_IS_ACTIVE);

        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCase.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCase))
            )
            .andExpect(status().isOk());

        // Validate the TestCase in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCaseUpdatableFieldsEquals(partialUpdatedTestCase, getPersistedTestCase(partialUpdatedTestCase));
    }

    @Test
    @Transactional
    void patchNonExistingTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCase() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCase.setId(longCount.incrementAndGet());

        // Create the TestCase
        TestCaseDTO testCaseDTO = testCaseMapper.toDto(testCase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testCaseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCase in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCase() throws Exception {
        // Initialize the database
        insertedTestCase = testCaseRepository.saveAndFlush(testCase);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testCase
        restTestCaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testCaseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TestCase getPersistedTestCase(TestCase testCase) {
        return testCaseRepository.findById(testCase.getId()).orElseThrow();
    }

    protected void assertPersistedTestCaseToMatchAllProperties(TestCase expectedTestCase) {
        assertTestCaseAllPropertiesEquals(expectedTestCase, getPersistedTestCase(expectedTestCase));
    }

    protected void assertPersistedTestCaseToMatchUpdatableProperties(TestCase expectedTestCase) {
        assertTestCaseAllUpdatablePropertiesEquals(expectedTestCase, getPersistedTestCase(expectedTestCase));
    }
}
