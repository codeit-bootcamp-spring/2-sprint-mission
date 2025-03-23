package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(config = CentralMapperConfig.class)
public interface ReadStatusMapping {
    ReadStatusMapping INSTANCE = Mappers.getMapper(ReadStatusMapping.class);

    ReadStatusDto.ResponseReadStatus toResponseDto(ReadStatus readStatus);

    ReadStatus createDtoToEntity(ReadStatusDto.Create createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ReadStatusDto.Update updateDto, @MappingTarget ReadStatus readStatus);
    

    ReadStatusDto.Update entityToUpdateDto(ReadStatus readStatus);
} 