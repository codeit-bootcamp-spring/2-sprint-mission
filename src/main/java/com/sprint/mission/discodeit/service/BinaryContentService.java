package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateDto binaryContentCreateDto);

    BinaryContent findById(UUID binaryContentId);

    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

    void delete(UUID binaryContentId);
}
