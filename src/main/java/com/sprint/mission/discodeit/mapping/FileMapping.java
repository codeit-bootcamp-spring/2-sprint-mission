package com.sprint.mission.discodeit.mapping;

import com.sprint.mission.discodeit.config.CentralMapperConfig;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;



@Mapper(config = CentralMapperConfig.class)
public interface FileMapping {
    FileMapping INSTANCE = Mappers.getMapper(FileMapping.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BinaryContent dtoToBinaryContent(BinaryContentDto.Create createDto, @MappingTarget BinaryContent binaryContent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BinaryContentDto.Summary binaryContentToSummary(BinaryContent binaryContent);

    @Mapping(target = "fileName", source = "binaryContent.fileName")
    @Mapping(target = "message", expression = "java(message)")
    BinaryContentDto.DeleteResponse binaryContentToDeleteResponse(BinaryContent binaryContent, @Context String message);
}
