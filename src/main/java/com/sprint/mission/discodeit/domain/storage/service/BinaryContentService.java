package com.sprint.mission.discodeit.domain.storage.service;

import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto create(BinaryContentCreateRequest request);

  BinaryContentDto find(UUID binaryContentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID binaryContentId);
}
