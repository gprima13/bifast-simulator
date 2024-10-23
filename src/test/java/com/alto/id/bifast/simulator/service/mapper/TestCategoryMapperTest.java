package com.alto.id.bifast.simulator.service.mapper;

import static com.alto.id.bifast.simulator.domain.TestCategoryAsserts.*;
import static com.alto.id.bifast.simulator.domain.TestCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCategoryMapperTest {

    private TestCategoryMapper testCategoryMapper;

    @BeforeEach
    void setUp() {
        testCategoryMapper = new TestCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTestCategorySample1();
        var actual = testCategoryMapper.toEntity(testCategoryMapper.toDto(expected));
        assertTestCategoryAllPropertiesEquals(expected, actual);
    }
}
