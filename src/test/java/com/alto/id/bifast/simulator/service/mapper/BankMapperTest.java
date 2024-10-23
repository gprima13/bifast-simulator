package com.alto.id.bifast.simulator.service.mapper;

import static com.alto.id.bifast.simulator.domain.BankAsserts.*;
import static com.alto.id.bifast.simulator.domain.BankTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankMapperTest {

    private BankMapper bankMapper;

    @BeforeEach
    void setUp() {
        bankMapper = new BankMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBankSample1();
        var actual = bankMapper.toEntity(bankMapper.toDto(expected));
        assertBankAllPropertiesEquals(expected, actual);
    }
}
