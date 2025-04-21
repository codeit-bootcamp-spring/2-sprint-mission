package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {
    @Override
    public BinaryContentDto toDto(BinaryContent binaryContent) {
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()

        );
    }
}