package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent binaryContent);

  List<BinaryContentDto> toDtoList(List<BinaryContent> binaryContents);
}
