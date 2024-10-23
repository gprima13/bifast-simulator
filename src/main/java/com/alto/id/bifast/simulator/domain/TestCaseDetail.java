package com.alto.id.bifast.simulator.domain;

import com.alto.id.bifast.simulator.domain.enumeration.ValueType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestCaseDetail.
 */
@Entity
@Table(name = "test_case_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matching_param")
    private String matchingParam;

    @Column(name = "matching_value")
    private String matchingValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private ValueType valueType;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testCaseDetails", "category" }, allowSetters = true)
    private TestCase testCase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCaseDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatchingParam() {
        return this.matchingParam;
    }

    public TestCaseDetail matchingParam(String matchingParam) {
        this.setMatchingParam(matchingParam);
        return this;
    }

    public void setMatchingParam(String matchingParam) {
        this.matchingParam = matchingParam;
    }

    public String getMatchingValue() {
        return this.matchingValue;
    }

    public TestCaseDetail matchingValue(String matchingValue) {
        this.setMatchingValue(matchingValue);
        return this;
    }

    public void setMatchingValue(String matchingValue) {
        this.matchingValue = matchingValue;
    }

    public ValueType getValueType() {
        return this.valueType;
    }

    public TestCaseDetail valueType(ValueType valueType) {
        this.setValueType(valueType);
        return this;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public TestCaseDetail isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TestCase getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public TestCaseDetail testCase(TestCase testCase) {
        this.setTestCase(testCase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCaseDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDetail{" +
            "id=" + getId() +
            ", matchingParam='" + getMatchingParam() + "'" +
            ", matchingValue='" + getMatchingValue() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
