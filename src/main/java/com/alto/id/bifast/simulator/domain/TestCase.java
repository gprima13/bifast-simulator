package com.alto.id.bifast.simulator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestCase.
 */
@Entity
@Table(name = "test_case")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "case_name")
    private String caseName;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testCase")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testCase" }, allowSetters = true)
    private Set<TestCaseDetail> testCaseDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testCases" }, allowSetters = true)
    private TestCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseName() {
        return this.caseName;
    }

    public TestCase caseName(String caseName) {
        this.setCaseName(caseName);
        return this;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public TestCase responseBody(String responseBody) {
        this.setResponseBody(responseBody);
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public TestCase transactionStatus(String transactionStatus) {
        this.setTransactionStatus(transactionStatus);
        return this;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public TestCase responseCode(String responseCode) {
        this.setResponseCode(responseCode);
        return this;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TestCase isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<TestCaseDetail> getTestCaseDetails() {
        return this.testCaseDetails;
    }

    public void setTestCaseDetails(Set<TestCaseDetail> testCaseDetails) {
        if (this.testCaseDetails != null) {
            this.testCaseDetails.forEach(i -> i.setTestCase(null));
        }
        if (testCaseDetails != null) {
            testCaseDetails.forEach(i -> i.setTestCase(this));
        }
        this.testCaseDetails = testCaseDetails;
    }

    public TestCase testCaseDetails(Set<TestCaseDetail> testCaseDetails) {
        this.setTestCaseDetails(testCaseDetails);
        return this;
    }

    public TestCase addTestCaseDetail(TestCaseDetail testCaseDetail) {
        this.testCaseDetails.add(testCaseDetail);
        testCaseDetail.setTestCase(this);
        return this;
    }

    public TestCase removeTestCaseDetail(TestCaseDetail testCaseDetail) {
        this.testCaseDetails.remove(testCaseDetail);
        testCaseDetail.setTestCase(null);
        return this;
    }

    public TestCategory getCategory() {
        return this.category;
    }

    public void setCategory(TestCategory testCategory) {
        this.category = testCategory;
    }

    public TestCase category(TestCategory testCategory) {
        this.setCategory(testCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCase)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCase) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCase{" +
            "id=" + getId() +
            ", caseName='" + getCaseName() + "'" +
            ", responseBody='" + getResponseBody() + "'" +
            ", transactionStatus='" + getTransactionStatus() + "'" +
            ", responseCode='" + getResponseCode() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
