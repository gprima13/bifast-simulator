package com.alto.id.bifast.simulator.service.impl;

import com.alto.id.bifast.simulator.domain.TestCase;
import com.alto.id.bifast.simulator.repository.TestCaseRepository;
import com.alto.id.bifast.simulator.service.TestCaseService;
import com.alto.id.bifast.simulator.service.dto.TestCaseDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.alto.id.bifast.simulator.domain.TestCase}.
 */
@Service
@Transactional
public class TestCaseServiceImpl implements TestCaseService {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseServiceImpl.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository, TestCaseMapper testCaseMapper) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
    }

    @Override
    public TestCaseDTO save(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to save TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toDto(testCase);
    }

    @Override
    public TestCaseDTO update(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to update TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        return testCaseMapper.toDto(testCase);
    }

    @Override
    public Optional<TestCaseDTO> partialUpdate(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to partially update TestCase : {}", testCaseDTO);

        return testCaseRepository
            .findById(testCaseDTO.getId())
            .map(existingTestCase -> {
                testCaseMapper.partialUpdate(existingTestCase, testCaseDTO);

                return existingTestCase;
            })
            .map(testCaseRepository::save)
            .map(testCaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestCaseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TestCases");
        return testCaseRepository.findAll(pageable).map(testCaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestCaseDTO> findOne(Long id) {
        LOG.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findById(id).map(testCaseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TestCase : {}", id);
        testCaseRepository.deleteById(id);
    }
}
