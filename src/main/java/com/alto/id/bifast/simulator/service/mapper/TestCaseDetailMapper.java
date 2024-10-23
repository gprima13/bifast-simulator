package com.alto.id.bifast.simulator.service.mapper;

import com.alto.id.bifast.simulator.domain.TestCase;
import com.alto.id.bifast.simulator.domain.TestCaseDetail;
import com.alto.id.bifast.simulator.service.dto.TestCaseDTO;
import com.alto.id.bifast.simulator.service.dto.TestCaseDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestCaseDetail} and its DTO {@link TestCaseDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestCaseDetailMapper extends EntityMapper<TestCaseDetailDTO, TestCaseDetail> {
    @Mapping(target = "testCase", source = "testCase", qualifiedByName = "testCaseId")
    TestCaseDetailDTO toDto(TestCaseDetail s);

    @Named("testCaseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TestCaseDTO toDtoTestCaseId(TestCase testCase);
}
