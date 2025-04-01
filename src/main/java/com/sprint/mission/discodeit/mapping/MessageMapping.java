package com.sprint.mission.discodeit.mapping;


import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(config = CentralMapperConfig.class)
public interface MessageMapping {
    MessageMapping INSTANCE = Mappers.getMapper(MessageMapping.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageDto.Response messageToResponse(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMessageFromDto(MessageDto.Update messageDto, @MappingTarget Message message);
}