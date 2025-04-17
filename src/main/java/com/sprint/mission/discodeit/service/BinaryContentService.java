package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateDto binaryContentCreateDto);

    BinaryContentDto findById(UUID binaryContentId);

    List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

    void delete(UUID binaryContentId);
}
