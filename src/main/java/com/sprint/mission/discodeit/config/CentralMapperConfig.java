package com.sprint.mission.discodeit.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * 모든 매퍼에 공통으로 적용되는 설정
 */
@MapperConfig(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CentralMapperConfig {
}

