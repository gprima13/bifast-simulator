package com.alto.id.bifast.simulator.domain;

import static com.alto.id.bifast.simulator.domain.BankTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.alto.id.bifast.simulator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = getBankSample1();
        Bank bank2 = new Bank();
        assertThat(bank1).isNotEqualTo(bank2);

        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);

        bank2 = getBankSample2();
        assertThat(bank1).isNotEqualTo(bank2);
    }
}
