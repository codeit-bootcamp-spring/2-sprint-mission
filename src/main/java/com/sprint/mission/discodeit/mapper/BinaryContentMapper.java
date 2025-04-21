package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentMapper {
    BinaryContentDto toDto(BinaryContent binaryContent);
}
