package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void createBinaryContent(BinaryContentCreateDto dto);

    BinaryContent findBinaryContent(UUID binaryContentId);

    List<BinaryContent> findAllBinaryContent();

    void deleteBinaryContent(UUID binaryContentId);
}
