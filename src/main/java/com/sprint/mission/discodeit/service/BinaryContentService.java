package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(BinaryContentCreateRequest createRequest);

    BinaryContent findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> idList);

    void delete(UUID id);
}
