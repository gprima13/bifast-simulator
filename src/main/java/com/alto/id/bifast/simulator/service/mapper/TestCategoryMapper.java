package com.alto.id.bifast.simulator.service.mapper;

import com.alto.id.bifast.simulator.domain.TestCategory;
import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestCategory} and its DTO {@link TestCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestCategoryMapper extends EntityMapper<TestCategoryDTO, TestCategory> {}
