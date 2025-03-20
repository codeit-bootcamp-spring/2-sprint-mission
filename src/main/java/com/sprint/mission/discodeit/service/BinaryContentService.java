package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDto create(BinaryContentCreateRequest request);
    BinaryContentDto find(UUID id);
    List<BinaryContentDto> findAllByIdIn(List<UUID> ids);
    void delete(UUID id);
}
