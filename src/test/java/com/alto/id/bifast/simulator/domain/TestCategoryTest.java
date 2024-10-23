package com.alto.id.bifast.simulator.domain;

import static com.alto.id.bifast.simulator.domain.TestCaseTestSamples.*;
import static com.alto.id.bifast.simulator.domain.TestCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCategory.class);
        TestCategory testCategory1 = getTestCategorySample1();
        TestCategory testCategory2 = new TestCategory();
        assertThat(testCategory1).isNotEqualTo(testCategory2);

        testCategory2.setId(testCategory1.getId());
        assertThat(testCategory1).isEqualTo(testCategory2);

        testCategory2 = getTestCategorySample2();
        assertThat(testCategory1).isNotEqualTo(testCategory2);
    }

    @Test
    void testCaseTest() {
        TestCategory testCategory = getTestCategoryRandomSampleGenerator();
        TestCase testCaseBack = getTestCaseRandomSampleGenerator();

        testCategory.addTestCase(testCaseBack);
        assertThat(testCategory.getTestCases()).containsOnly(testCaseBack);
        assertThat(testCaseBack.getCategory()).isEqualTo(testCategory);

        testCategory.removeTestCase(testCaseBack);
        assertThat(testCategory.getTestCases()).doesNotContain(testCaseBack);
        assertThat(testCaseBack.getCategory()).isNull();

        testCategory.testCases(new HashSet<>(Set.of(testCaseBack)));
        assertThat(testCategory.getTestCases()).containsOnly(testCaseBack);
        assertThat(testCaseBack.getCategory()).isEqualTo(testCategory);

        testCategory.setTestCases(new HashSet<>());
        assertThat(testCategory.getTestCases()).doesNotContain(testCaseBack);
        assertThat(testCaseBack.getCategory()).isNull();
    }
}
