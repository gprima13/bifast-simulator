package com.alto.id.bifast.simulator.service.mapper;

import com.alto.id.bifast.simulator.domain.TestCase;
import com.alto.id.bifast.simulator.domain.TestCategory;
import com.alto.id.bifast.simulator.service.dto.TestCaseDTO;
import com.alto.id.bifast.simulator.service.dto.TestCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestCase} and its DTO {@link TestCaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestCaseMapper extends EntityMapper<TestCaseDTO, TestCase> {
    @Mapping(target = "category", source = "category", qualifiedByName = "testCategoryId")
    TestCaseDTO toDto(TestCase s);

    @Named("testCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TestCategoryDTO toDtoTestCategoryId(TestCategory testCategory);
}
