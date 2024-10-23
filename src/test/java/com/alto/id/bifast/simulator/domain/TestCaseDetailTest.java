package com.alto.id.bifast.simulator.domain;

import static com.alto.id.bifast.simulator.domain.TestCaseDetailTestSamples.*;
import static com.alto.id.bifast.simulator.domain.TestCaseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseDetail.class);
        TestCaseDetail testCaseDetail1 = getTestCaseDetailSample1();
        TestCaseDetail testCaseDetail2 = new TestCaseDetail();
        assertThat(testCaseDetail1).isNotEqualTo(testCaseDetail2);

        testCaseDetail2.setId(testCaseDetail1.getId());
        assertThat(testCaseDetail1).isEqualTo(testCaseDetail2);

        testCaseDetail2 = getTestCaseDetailSample2();
        assertThat(testCaseDetail1).isNotEqualTo(testCaseDetail2);
    }

    @Test
    void testCaseTest() {
        TestCaseDetail testCaseDetail = getTestCaseDetailRandomSampleGenerator();
        TestCase testCaseBack = getTestCaseRandomSampleGenerator();

        testCaseDetail.setTestCase(testCaseBack);
        assertThat(testCaseDetail.getTestCase()).isEqualTo(testCaseBack);

        testCaseDetail.testCase(null);
        assertThat(testCaseDetail.getTestCase()).isNull();
    }
}
