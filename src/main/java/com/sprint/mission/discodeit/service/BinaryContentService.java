package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity._BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  _BinaryContent create(BinaryContentCreateRequest request);

  _BinaryContent find(UUID binaryContentId);

  List<_BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

  void delete(UUID binaryContentId);
}
