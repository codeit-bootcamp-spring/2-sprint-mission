package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest createRequest);

  BinaryContentDto findById(UUID id);

  List<BinaryContentDto> findAllByIdIn(List<UUID> idList);

  BinaryContent findContentById(UUID id);

  void delete(UUID id);
}
