package com.alto.id.bifast.simulator.service.impl;

import com.alto.id.bifast.simulator.domain.TestCaseDetail;
import com.alto.id.bifast.simulator.repository.TestCaseDetailRepository;
import com.alto.id.bifast.simulator.service.TestCaseDetailService;
import com.alto.id.bifast.simulator.service.dto.TestCaseDetailDTO;
import com.alto.id.bifast.simulator.service.mapper.TestCaseDetailMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.alto.id.bifast.simulator.domain.TestCaseDetail}.
 */
@Service
@Transactional
public class TestCaseDetailServiceImpl implements TestCaseDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseDetailServiceImpl.class);

    private final TestCaseDetailRepository testCaseDetailRepository;

    private final TestCaseDetailMapper testCaseDetailMapper;

    public TestCaseDetailServiceImpl(TestCaseDetailRepository testCaseDetailRepository, TestCaseDetailMapper testCaseDetailMapper) {
        this.testCaseDetailRepository = testCaseDetailRepository;
        this.testCaseDetailMapper = testCaseDetailMapper;
    }

    @Override
    public TestCaseDetailDTO save(TestCaseDetailDTO testCaseDetailDTO) {
        LOG.debug("Request to save TestCaseDetail : {}", testCaseDetailDTO);
        TestCaseDetail testCaseDetail = testCaseDetailMapper.toEntity(testCaseDetailDTO);
        testCaseDetail = testCaseDetailRepository.save(testCaseDetail);
        return testCaseDetailMapper.toDto(testCaseDetail);
    }

    @Override
    public TestCaseDetailDTO update(TestCaseDetailDTO testCaseDetailDTO) {
        LOG.debug("Request to update TestCaseDetail : {}", testCaseDetailDTO);
        TestCaseDetail testCaseDetail = testCaseDetailMapper.toEntity(testCaseDetailDTO);
        testCaseDetail = testCaseDetailRepository.save(testCaseDetail);
        return testCaseDetailMapper.toDto(testCaseDetail);
    }

    @Override
    public Optional<TestCaseDetailDTO> partialUpdate(TestCaseDetailDTO testCaseDetailDTO) {
        LOG.debug("Request to partially update TestCaseDetail : {}", testCaseDetailDTO);

        return testCaseDetailRepository
            .findById(testCaseDetailDTO.getId())
            .map(existingTestCaseDetail -> {
                testCaseDetailMapper.partialUpdate(existingTestCaseDetail, testCaseDetailDTO);

                return existingTestCaseDetail;
            })
            .map(testCaseDetailRepository::save)
            .map(testCaseDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestCaseDetailDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TestCaseDetails");
        return testCaseDetailRepository.findAll(pageable).map(testCaseDetailMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestCaseDetailDTO> findOne(Long id) {
        LOG.debug("Request to get TestCaseDetail : {}", id);
        return testCaseDetailRepository.findById(id).map(testCaseDetailMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TestCaseDetail : {}", id);
        testCaseDetailRepository.deleteById(id);
    }
}
