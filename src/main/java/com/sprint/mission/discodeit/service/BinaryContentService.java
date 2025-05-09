package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentResult;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResult create(BinaryContentRequest binaryContentRequest);

    BinaryContentResult getById(UUID id);

    List<BinaryContentResult> getByIdIn(List<UUID> ids);

    void delete(UUID id);
}
