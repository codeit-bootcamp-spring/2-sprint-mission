package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest createRequest);

  BinaryContentResponse findById(UUID id);

  List<BinaryContentResponse> findAllByIdIn(List<UUID> idList);

  void delete(UUID id);
}
