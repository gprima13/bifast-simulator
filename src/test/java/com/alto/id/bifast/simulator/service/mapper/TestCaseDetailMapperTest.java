package com.alto.id.bifast.simulator.service.mapper;

import static com.alto.id.bifast.simulator.domain.TestCaseDetailAsserts.*;
import static com.alto.id.bifast.simulator.domain.TestCaseDetailTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCaseDetailMapperTest {

    private TestCaseDetailMapper testCaseDetailMapper;

    @BeforeEach
    void setUp() {
        testCaseDetailMapper = new TestCaseDetailMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTestCaseDetailSample1();
        var actual = testCaseDetailMapper.toEntity(testCaseDetailMapper.toDto(expected));
        assertTestCaseDetailAllPropertiesEquals(expected, actual);
    }
}
