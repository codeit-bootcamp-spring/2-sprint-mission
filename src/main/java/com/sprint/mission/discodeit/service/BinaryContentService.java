package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest request);

  BinaryContent find(UUID binaryContentId);

  List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

  ResponseEntity<Resource> download(UUID id);

  void delete(UUID binaryContentId);
}
