package com.alto.id.bifast.simulator.domain;

import static com.alto.id.bifast.simulator.domain.TestCaseDetailTestSamples.*;
import static com.alto.id.bifast.simulator.domain.TestCaseTestSamples.*;
import static com.alto.id.bifast.simulator.domain.TestCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestCaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCase.class);
        TestCase testCase1 = getTestCaseSample1();
        TestCase testCase2 = new TestCase();
        assertThat(testCase1).isNotEqualTo(testCase2);

        testCase2.setId(testCase1.getId());
        assertThat(testCase1).isEqualTo(testCase2);

        testCase2 = getTestCaseSample2();
        assertThat(testCase1).isNotEqualTo(testCase2);
    }

    @Test
    void testCaseDetailTest() {
        TestCase testCase = getTestCaseRandomSampleGenerator();
        TestCaseDetail testCaseDetailBack = getTestCaseDetailRandomSampleGenerator();

        testCase.addTestCaseDetail(testCaseDetailBack);
        assertThat(testCase.getTestCaseDetails()).containsOnly(testCaseDetailBack);
        assertThat(testCaseDetailBack.getTestCase()).isEqualTo(testCase);

        testCase.removeTestCaseDetail(testCaseDetailBack);
        assertThat(testCase.getTestCaseDetails()).doesNotContain(testCaseDetailBack);
        assertThat(testCaseDetailBack.getTestCase()).isNull();

        testCase.testCaseDetails(new HashSet<>(Set.of(testCaseDetailBack)));
        assertThat(testCase.getTestCaseDetails()).containsOnly(testCaseDetailBack);
        assertThat(testCaseDetailBack.getTestCase()).isEqualTo(testCase);

        testCase.setTestCaseDetails(new HashSet<>());
        assertThat(testCase.getTestCaseDetails()).doesNotContain(testCaseDetailBack);
        assertThat(testCaseDetailBack.getTestCase()).isNull();
    }

    @Test
    void categoryTest() {
        TestCase testCase = getTestCaseRandomSampleGenerator();
        TestCategory testCategoryBack = getTestCategoryRandomSampleGenerator();

        testCase.setCategory(testCategoryBack);
        assertThat(testCase.getCategory()).isEqualTo(testCategoryBack);

        testCase.category(null);
        assertThat(testCase.getCategory()).isNull();
    }
}
