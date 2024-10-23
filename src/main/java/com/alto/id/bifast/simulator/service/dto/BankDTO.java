package com.alto.id.bifast.simulator.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.alto.id.bifast.simulator.domain.Bank} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankDTO implements Serializable {

    private Long id;

    private String bicCode;

    private String bankName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankDTO)) {
            return false;
        }

        BankDTO bankDTO = (BankDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + getId() +
            ", bicCode='" + getBicCode() + "'" +
            ", bankName='" + getBankName() + "'" +
            "}";
    }
}
