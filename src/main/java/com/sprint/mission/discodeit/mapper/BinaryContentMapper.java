package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);

  FindBinaryContentResponseDTO toFindBinaryContentResponseDTO(
      FindBinaryContentResult findBinaryContentResult);

  CreateBinaryContentResult toCreateBinaryContentResult(BinaryContent binaryContent);

  FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent);
}
