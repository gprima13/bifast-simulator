package com.alto.id.bifast.simulator.service.impl;

import com.alto.id.bifast.simulator.domain.TestCategory;
import com.alto.id.bifast.simulator.repository.TestCategoryRepository;
import com.alto.id.bifast.simulator.service.TestCategoryService;
import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCategoryMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.alto.id.bifast.simulator.domain.TestCategory}.
 */
@Service
@Transactional
public class TestCategoryServiceImpl implements TestCategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(TestCategoryServiceImpl.class);

    private final TestCategoryRepository testCategoryRepository;

    private final TestCategoryMapper testCategoryMapper;

    public TestCategoryServiceImpl(TestCategoryRepository testCategoryRepository, TestCategoryMapper testCategoryMapper) {
        this.testCategoryRepository = testCategoryRepository;
        this.testCategoryMapper = testCategoryMapper;
    }

    @Override
    public TestCategoryDTO save(TestCategoryDTO testCategoryDTO) {
        LOG.debug("Request to save TestCategory : {}", testCategoryDTO);
        TestCategory testCategory = testCategoryMapper.toEntity(testCategoryDTO);
        testCategory = testCategoryRepository.save(testCategory);
        return testCategoryMapper.toDto(testCategory);
    }

    @Override
    public TestCategoryDTO update(TestCategoryDTO testCategoryDTO) {
        LOG.debug("Request to update TestCategory : {}", testCategoryDTO);
        TestCategory testCategory = testCategoryMapper.toEntity(testCategoryDTO);
        testCategory = testCategoryRepository.save(testCategory);
        return testCategoryMapper.toDto(testCategory);
    }

    @Override
    public Optional<TestCategoryDTO> partialUpdate(TestCategoryDTO testCategoryDTO) {
        LOG.debug("Request to partially update TestCategory : {}", testCategoryDTO);

        return testCategoryRepository
            .findById(testCategoryDTO.getId())
            .map(existingTestCategory -> {
                testCategoryMapper.partialUpdate(existingTestCategory, testCategoryDTO);

                return existingTestCategory;
            })
            .map(testCategoryRepository::save)
            .map(testCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCategoryDTO> findAll() {
        LOG.debug("Request to get all TestCategories");
        return testCategoryRepository.findAll().stream().map(testCategoryMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestCategoryDTO> findOne(Long id) {
        LOG.debug("Request to get TestCategory : {}", id);
        return testCategoryRepository.findById(id).map(testCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TestCategory : {}", id);
        testCategoryRepository.deleteById(id);
    }
}
