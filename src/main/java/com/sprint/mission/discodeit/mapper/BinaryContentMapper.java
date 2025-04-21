package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

    public BinaryContentDto toDto(BinaryContent entity) {
        if (entity == null) {
            return null;
        }

        return new BinaryContentDto(
            entity.getId(),
            entity.getFileName(),
            entity.getSize(),
            entity.getContentType()
        );
    }

}
