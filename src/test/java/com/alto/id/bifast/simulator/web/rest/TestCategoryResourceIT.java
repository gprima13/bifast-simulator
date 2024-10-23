package com.alto.id.bifast.simulator.web.rest;

import static com.alto.id.bifast.simulator.domain.TestCategoryAsserts.*;
import static com.alto.id.bifast.simulator.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alto.id.bifast.simulator.IntegrationTest;
import com.alto.id.bifast.simulator.domain.TestCategory;
import com.alto.id.bifast.simulator.repository.TestCategoryRepository;
import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCategoryMapper;
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
 * Integration tests for the {@link TestCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCategoryResourceIT {

    private static final String DEFAULT_TEST_SUITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEST_SUITE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/test-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestCategoryRepository testCategoryRepository;

    @Autowired
    private TestCategoryMapper testCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCategoryMockMvc;

    private TestCategory testCategory;

    private TestCategory insertedTestCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCategory createEntity() {
        return new TestCategory().testSuiteName(DEFAULT_TEST_SUITE_NAME).isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCategory createUpdatedEntity() {
        return new TestCategory().testSuiteName(UPDATED_TEST_SUITE_NAME).isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        testCategory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTestCategory != null) {
            testCategoryRepository.delete(insertedTestCategory);
            insertedTestCategory = null;
        }
    }

    @Test
    @Transactional
    void createTestCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);
        var returnedTestCategoryDTO = om.readValue(
            restTestCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestCategoryDTO.class
        );

        // Validate the TestCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTestCategory = testCategoryMapper.toEntity(returnedTestCategoryDTO);
        assertTestCategoryUpdatableFieldsEquals(returnedTestCategory, getPersistedTestCategory(returnedTestCategory));

        insertedTestCategory = returnedTestCategory;
    }

    @Test
    @Transactional
    void createTestCategoryWithExistingId() throws Exception {
        // Create the TestCategory with an existing ID
        testCategory.setId(1L);
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCategories() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        // Get all the testCategoryList
        restTestCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].testSuiteName").value(hasItem(DEFAULT_TEST_SUITE_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getTestCategory() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        // Get the testCategory
        restTestCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, testCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCategory.getId().intValue()))
            .andExpect(jsonPath("$.testSuiteName").value(DEFAULT_TEST_SUITE_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestCategory() throws Exception {
        // Get the testCategory
        restTestCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCategory() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCategory
        TestCategory updatedTestCategory = testCategoryRepository.findById(testCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCategory are not directly saved in db
        em.detach(updatedTestCategory);
        updatedTestCategory.testSuiteName(UPDATED_TEST_SUITE_NAME).isActive(UPDATED_IS_ACTIVE);
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(updatedTestCategory);

        restTestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestCategoryToMatchAllProperties(updatedTestCategory);
    }

    @Test
    @Transactional
    void putNonExistingTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCategory using partial update
        TestCategory partialUpdatedTestCategory = new TestCategory();
        partialUpdatedTestCategory.setId(testCategory.getId());

        partialUpdatedTestCategory.isActive(UPDATED_IS_ACTIVE);

        restTestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCategory))
            )
            .andExpect(status().isOk());

        // Validate the TestCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestCategory, testCategory),
            getPersistedTestCategory(testCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCategory using partial update
        TestCategory partialUpdatedTestCategory = new TestCategory();
        partialUpdatedTestCategory.setId(testCategory.getId());

        partialUpdatedTestCategory.testSuiteName(UPDATED_TEST_SUITE_NAME).isActive(UPDATED_IS_ACTIVE);

        restTestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCategory))
            )
            .andExpect(status().isOk());

        // Validate the TestCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCategoryUpdatableFieldsEquals(partialUpdatedTestCategory, getPersistedTestCategory(partialUpdatedTestCategory));
    }

    @Test
    @Transactional
    void patchNonExistingTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCategory.setId(longCount.incrementAndGet());

        // Create the TestCategory
        TestCategoryDTO testCategoryDTO = testCategoryMapper.toDto(testCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCategory() throws Exception {
        // Initialize the database
        insertedTestCategory = testCategoryRepository.saveAndFlush(testCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testCategory
        restTestCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testCategoryRepository.count();
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

    protected TestCategory getPersistedTestCategory(TestCategory testCategory) {
        return testCategoryRepository.findById(testCategory.getId()).orElseThrow();
    }

    protected void assertPersistedTestCategoryToMatchAllProperties(TestCategory expectedTestCategory) {
        assertTestCategoryAllPropertiesEquals(expectedTestCategory, getPersistedTestCategory(expectedTestCategory));
    }

    protected void assertPersistedTestCategoryToMatchUpdatableProperties(TestCategory expectedTestCategory) {
        assertTestCategoryAllUpdatablePropertiesEquals(expectedTestCategory, getPersistedTestCategory(expectedTestCategory));
    }
}
