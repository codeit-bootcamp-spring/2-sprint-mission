package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResult createBinaryContent(BinaryContentRequest binaryContentRequest);

    BinaryContentResult getById(UUID id);

    List<BinaryContentResult> getByIdIn(List<UUID> ids);

    void delete(UUID id);

}
