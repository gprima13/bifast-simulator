package com.alto.id.bifast.simulator.service.mapper;

import static com.alto.id.bifast.simulator.domain.TestCaseAsserts.*;
import static com.alto.id.bifast.simulator.domain.TestCaseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCaseMapperTest {

    private TestCaseMapper testCaseMapper;

    @BeforeEach
    void setUp() {
        testCaseMapper = new TestCaseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTestCaseSample1();
        var actual = testCaseMapper.toEntity(testCaseMapper.toDto(expected));
        assertTestCaseAllPropertiesEquals(expected, actual);
    }
}
