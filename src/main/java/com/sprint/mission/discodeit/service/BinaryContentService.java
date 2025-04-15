package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest request);

  BinaryContent find(UUID binaryContentKey);

  List<BinaryContent> findAllByKeys(List<UUID> binaryContentKeys);

  void delete(UUID binaryContentKey);
}
