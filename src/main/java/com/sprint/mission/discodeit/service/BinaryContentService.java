package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void create(BinaryContentDto binaryContentCreateDto);
    BinaryContentDto find(UUID id);
    List<BinaryContentDto> findAllByIdIn(UUID userId);
    void delete(UUID id);
}
