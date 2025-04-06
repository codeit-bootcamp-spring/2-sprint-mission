package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent save(BinaryContentCreateRequest binaryContentCreateRequest);

  BinaryContent findById(UUID binaryContentUUID);

  List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList);

  void delete(UUID userStatusUUID);
}
