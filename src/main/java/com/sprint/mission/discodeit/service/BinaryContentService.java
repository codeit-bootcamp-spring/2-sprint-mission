package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.BinaryContentCreateParam;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    List<UUID> create(BinaryContentCreateParam createParam);

    BinaryContent findMetaDataById(UUID id);

    List<BinaryContent> findAllMetaDataByIdIn(List<UUID> idList);

    void delete(UUID id);
}
