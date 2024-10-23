package com.alto.id.bifast.simulator.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseDetailDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseDetailDTO.class);
        TestCaseDetailDTO testCaseDetailDTO1 = new TestCaseDetailDTO();
        testCaseDetailDTO1.setId(1L);
        TestCaseDetailDTO testCaseDetailDTO2 = new TestCaseDetailDTO();
        assertThat(testCaseDetailDTO1).isNotEqualTo(testCaseDetailDTO2);
        testCaseDetailDTO2.setId(testCaseDetailDTO1.getId());
        assertThat(testCaseDetailDTO1).isEqualTo(testCaseDetailDTO2);
        testCaseDetailDTO2.setId(2L);
        assertThat(testCaseDetailDTO1).isNotEqualTo(testCaseDetailDTO2);
        testCaseDetailDTO1.setId(null);
        assertThat(testCaseDetailDTO1).isNotEqualTo(testCaseDetailDTO2);
    }
}
