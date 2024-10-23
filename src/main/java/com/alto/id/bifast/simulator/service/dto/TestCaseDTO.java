package com.alto.id.bifast.simulator.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.alto.id.bifast.simulator.domain.TestCase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseDTO implements Serializable {

    private Long id;

    private String caseName;

    private String responseBody;

    private String transactionStatus;

    private String responseCode;

    private Boolean isActive;

    private TestCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TestCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(TestCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDTO)) {
            return false;
        }

        TestCaseDTO testCaseDTO = (TestCaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDTO{" +
            "id=" + getId() +
            ", caseName='" + getCaseName() + "'" +
            ", responseBody='" + getResponseBody() + "'" +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", responseCode='" + getResponseCode() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
