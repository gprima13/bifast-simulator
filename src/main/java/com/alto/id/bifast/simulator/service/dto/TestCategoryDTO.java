package com.alto.id.bifast.simulator.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.alto.id.bifast.simulator.domain.TestCategory} entity.
 */
@Schema(description = "The Test Category entity, contains the category for test\neg: accountInquiry, creditTransfer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCategoryDTO implements Serializable {

    private Long id;

    private String testSuiteName;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCategoryDTO)) {
            return false;
        }

        TestCategoryDTO testCategoryDTO = (TestCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCategoryDTO{" +
            "id=" + getId() +
            ", testSuiteName='" + getTestSuiteName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
