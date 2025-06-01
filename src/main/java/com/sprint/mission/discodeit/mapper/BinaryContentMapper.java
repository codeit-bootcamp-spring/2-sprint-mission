package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
    @Mapping(source = "fileName", target = "fileName")
    @Mapping(source = "contentType", target = "contentType")
    BinaryContentDto toDto(BinaryContent binaryContent);
}
