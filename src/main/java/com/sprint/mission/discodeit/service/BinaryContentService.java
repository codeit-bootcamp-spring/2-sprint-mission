package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto createBinaryContent(BinaryContentCreateRequest request);

  BinaryContentDto findById(UUID binaryContentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

  void deleteBinaryContent(UUID binaryContentId);

}
