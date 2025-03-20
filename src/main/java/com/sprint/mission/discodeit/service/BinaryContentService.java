package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateParam;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    List<UUID> create(BinaryContentCreateParam createParam);

    BinaryContent findById(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> idList);

    void delete(UUID id);
}
