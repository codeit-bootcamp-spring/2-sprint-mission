package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  CreateBinaryContentResult create(BinaryContent binaryContent);

  FindBinaryContentResult find(UUID id);

  List<FindBinaryContentResult> findAllByIdIn(List<UUID> attachmentsId);

  void delete(UUID id);
}
