package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.BinaryContentResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentService {
    void createBinaryContent(BinaryContentCreateDto createDto);
    BinaryContentResponseDto findById(UUID id);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> idList);
    void deleteBinaryContent(UUID id);
    List<byte[]> findAll();
}
