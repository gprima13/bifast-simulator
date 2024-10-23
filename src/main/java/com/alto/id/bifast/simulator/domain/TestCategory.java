package com.alto.id.bifast.simulator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Test Category entity, contains the category for test
 * eg: accountInquiry, creditTransfer
 */
@Entity
@Table(name = "test_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "test_suite_name")
    private String testSuiteName;

    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * A relationship
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testCaseDetails", "category" }, allowSetters = true)
    private Set<TestCase> testCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestSuiteName() {
        return this.testSuiteName;
    }

    public TestCategory testSuiteName(String testSuiteName) {
        this.setTestSuiteName(testSuiteName);
        return this;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TestCategory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<TestCase> getTestCases() {
        return this.testCases;
    }

    public void setTestCases(Set<TestCase> testCases) {
        if (this.testCases != null) {
            this.testCases.forEach(i -> i.setCategory(null));
        }
        if (testCases != null) {
            testCases.forEach(i -> i.setCategory(this));
        }
        this.testCases = testCases;
    }

    public TestCategory testCases(Set<TestCase> testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public TestCategory addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.setCategory(this);
        return this;
    }

    public TestCategory removeTestCase(TestCase testCase) {
        this.testCases.remove(testCase);
        testCase.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCategory{" +
            "id=" + getId() +
            ", testSuiteName='" + getTestSuiteName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
