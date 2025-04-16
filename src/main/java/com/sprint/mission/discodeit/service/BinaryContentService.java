package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentDto findById(UUID binaryContentUUID);

  List<BinaryContentDto> findByIdIn(List<UUID> binaryContentUUIDList);
}
