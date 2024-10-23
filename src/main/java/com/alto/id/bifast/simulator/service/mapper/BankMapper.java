package com.alto.id.bifast.simulator.service.mapper;

import com.alto.id.bifast.simulator.domain.Bank;
import com.alto.id.bifast.simulator.service.dto.BankDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bank} and its DTO {@link BankDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankMapper extends EntityMapper<BankDTO, Bank> {}
