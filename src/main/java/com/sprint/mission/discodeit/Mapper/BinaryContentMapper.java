package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {
    public BinaryContentDto toDto(BinaryContent binaryContent) {
        return BinaryContentDto.builder()
                .id(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getSize())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getBytes())
                .build();
    }
}
