package com.alto.id.bifast.simulator.service.dto;

import com.alto.id.bifast.simulator.domain.enumeration.ValueType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.alto.id.bifast.simulator.domain.TestCaseDetail} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseDetailDTO implements Serializable {

    private Long id;

    private String matchingParam;

    private String matchingValue;

    private ValueType valueType;

    private Boolean isActive;

    private TestCaseDTO testCase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatchingParam() {
        return matchingParam;
    }

    public void setMatchingParam(String matchingParam) {
        this.matchingParam = matchingParam;
    }

    public String getMatchingValue() {
        return matchingValue;
    }

    public void setMatchingValue(String matchingValue) {
        this.matchingValue = matchingValue;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TestCaseDTO getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCaseDTO testCase) {
        this.testCase = testCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDetailDTO)) {
            return false;
        }

        TestCaseDetailDTO testCaseDetailDTO = (TestCaseDetailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCaseDetailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDetailDTO{" +
            "id=" + getId() +
            ", matchingParam='" + getMatchingParam() + "'" +
            ", matchingValue='" + getMatchingValue() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", testCase=" + getTestCase() +
            "}";
    }
}
