package com.alto.id.bifast.simulator.web.rest;

import static com.alto.id.bifast.simulator.domain.TestCaseDetailAsserts.*;
import static com.alto.id.bifast.simulator.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alto.id.bifast.simulator.IntegrationTest;
import com.alto.id.bifast.simulator.domain.TestCaseDetail;
import com.alto.id.bifast.simulator.domain.enumeration.ValueType;
import com.alto.id.bifast.simulator.repository.TestCaseDetailRepository;
import com.alto.id.bifast.simulator.service.dto.TestCaseDetailDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCaseDetailMapper;
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
 * Integration tests for the {@link TestCaseDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCaseDetailResourceIT {

    private static final String DEFAULT_MATCHING_PARAM = "AAAAAAAAAA";
    private static final String UPDATED_MATCHING_PARAM = "BBBBBBBBBB";

    private static final String DEFAULT_MATCHING_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_MATCHING_VALUE = "BBBBBBBBBB";

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.ALPHANUMERIC;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.NUMERIC;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/test-case-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestCaseDetailRepository testCaseDetailRepository;

    @Autowired
    private TestCaseDetailMapper testCaseDetailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCaseDetailMockMvc;

    private TestCaseDetail testCaseDetail;

    private TestCaseDetail insertedTestCaseDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseDetail createEntity() {
        return new TestCaseDetail()
            .matchingParam(DEFAULT_MATCHING_PARAM)
            .matchingValue(DEFAULT_MATCHING_VALUE)
            .valueType(DEFAULT_VALUE_TYPE)
            .isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCaseDetail createUpdatedEntity() {
        return new TestCaseDetail()
            .matchingParam(UPDATED_MATCHING_PARAM)
            .matchingValue(UPDATED_MATCHING_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        testCaseDetail = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTestCaseDetail != null) {
            testCaseDetailRepository.delete(insertedTestCaseDetail);
            insertedTestCaseDetail = null;
        }
    }

    @Test
    @Transactional
    void createTestCaseDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);
        var returnedTestCaseDetailDTO = om.readValue(
            restTestCaseDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestCaseDetailDTO.class
        );

        // Validate the TestCaseDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTestCaseDetail = testCaseDetailMapper.toEntity(returnedTestCaseDetailDTO);
        assertTestCaseDetailUpdatableFieldsEquals(returnedTestCaseDetail, getPersistedTestCaseDetail(returnedTestCaseDetail));

        insertedTestCaseDetail = returnedTestCaseDetail;
    }

    @Test
    @Transactional
    void createTestCaseDetailWithExistingId() throws Exception {
        // Create the TestCaseDetail with an existing ID
        testCaseDetail.setId(1L);
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCaseDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCaseDetails() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        // Get all the testCaseDetailList
        restTestCaseDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCaseDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].matchingParam").value(hasItem(DEFAULT_MATCHING_PARAM)))
            .andExpect(jsonPath("$.[*].matchingValue").value(hasItem(DEFAULT_MATCHING_VALUE)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getTestCaseDetail() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        // Get the testCaseDetail
        restTestCaseDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, testCaseDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCaseDetail.getId().intValue()))
            .andExpect(jsonPath("$.matchingParam").value(DEFAULT_MATCHING_PARAM))
            .andExpect(jsonPath("$.matchingValue").value(DEFAULT_MATCHING_VALUE))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestCaseDetail() throws Exception {
        // Get the testCaseDetail
        restTestCaseDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCaseDetail() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCaseDetail
        TestCaseDetail updatedTestCaseDetail = testCaseDetailRepository.findById(testCaseDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCaseDetail are not directly saved in db
        em.detach(updatedTestCaseDetail);
        updatedTestCaseDetail
            .matchingParam(UPDATED_MATCHING_PARAM)
            .matchingValue(UPDATED_MATCHING_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(updatedTestCaseDetail);

        restTestCaseDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestCaseDetailToMatchAllProperties(updatedTestCaseDetail);
    }

    @Test
    @Transactional
    void putNonExistingTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCaseDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCaseDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCaseDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCaseDetailWithPatch() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCaseDetail using partial update
        TestCaseDetail partialUpdatedTestCaseDetail = new TestCaseDetail();
        partialUpdatedTestCaseDetail.setId(testCaseDetail.getId());

        partialUpdatedTestCaseDetail
            .matchingParam(UPDATED_MATCHING_PARAM)
            .matchingValue(UPDATED_MATCHING_VALUE)
            .isActive(UPDATED_IS_ACTIVE);

        restTestCaseDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCaseDetail))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCaseDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestCaseDetail, testCaseDetail),
            getPersistedTestCaseDetail(testCaseDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestCaseDetailWithPatch() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCaseDetail using partial update
        TestCaseDetail partialUpdatedTestCaseDetail = new TestCaseDetail();
        partialUpdatedTestCaseDetail.setId(testCaseDetail.getId());

        partialUpdatedTestCaseDetail
            .matchingParam(UPDATED_MATCHING_PARAM)
            .matchingValue(UPDATED_MATCHING_VALUE)
            .valueType(UPDATED_VALUE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restTestCaseDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCaseDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCaseDetail))
            )
            .andExpect(status().isOk());

        // Validate the TestCaseDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCaseDetailUpdatableFieldsEquals(partialUpdatedTestCaseDetail, getPersistedTestCaseDetail(partialUpdatedTestCaseDetail));
    }

    @Test
    @Transactional
    void patchNonExistingTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCaseDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCaseDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCaseDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCaseDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCaseDetail.setId(longCount.incrementAndGet());

        // Create the TestCaseDetail
        TestCaseDetailDTO testCaseDetailDTO = testCaseDetailMapper.toDto(testCaseDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCaseDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testCaseDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCaseDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCaseDetail() throws Exception {
        // Initialize the database
        insertedTestCaseDetail = testCaseDetailRepository.saveAndFlush(testCaseDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testCaseDetail
        restTestCaseDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCaseDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testCaseDetailRepository.count();
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

    protected TestCaseDetail getPersistedTestCaseDetail(TestCaseDetail testCaseDetail) {
        return testCaseDetailRepository.findById(testCaseDetail.getId()).orElseThrow();
    }

    protected void assertPersistedTestCaseDetailToMatchAllProperties(TestCaseDetail expectedTestCaseDetail) {
        assertTestCaseDetailAllPropertiesEquals(expectedTestCaseDetail, getPersistedTestCaseDetail(expectedTestCaseDetail));
    }

    protected void assertPersistedTestCaseDetailToMatchUpdatableProperties(TestCaseDetail expectedTestCaseDetail) {
        assertTestCaseDetailAllUpdatablePropertiesEquals(expectedTestCaseDetail, getPersistedTestCaseDetail(expectedTestCaseDetail));
    }
}
