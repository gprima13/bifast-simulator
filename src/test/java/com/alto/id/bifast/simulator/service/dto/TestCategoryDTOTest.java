package com.alto.id.bifast.simulator.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCategoryDTO.class);
        TestCategoryDTO testCategoryDTO1 = new TestCategoryDTO();
        testCategoryDTO1.setId(1L);
        TestCategoryDTO testCategoryDTO2 = new TestCategoryDTO();
        assertThat(testCategoryDTO1).isNotEqualTo(testCategoryDTO2);
        testCategoryDTO2.setId(testCategoryDTO1.getId());
        assertThat(testCategoryDTO1).isEqualTo(testCategoryDTO2);
        testCategoryDTO2.setId(2L);
        assertThat(testCategoryDTO1).isNotEqualTo(testCategoryDTO2);
        testCategoryDTO1.setId(null);
        assertThat(testCategoryDTO1).isNotEqualTo(testCategoryDTO2);
    }
}
