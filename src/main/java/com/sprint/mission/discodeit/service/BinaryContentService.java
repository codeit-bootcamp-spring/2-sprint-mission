package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;

import java.util.UUID;
import java.util.List;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest requestDto);
    BinaryContent find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
}
