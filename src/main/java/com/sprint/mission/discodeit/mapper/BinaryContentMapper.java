package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
    BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);

    FindBinaryContentResponseDTO toBinaryContentResponseDTO(BinaryContentDTO binaryContentDTO);

}
