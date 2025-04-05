package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent createBinaryContent(BinaryContentCreateRequest request);

  BinaryContent findById(UUID binaryContentId);

  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

  void deleteBinaryContent(UUID binaryContentId);

}
