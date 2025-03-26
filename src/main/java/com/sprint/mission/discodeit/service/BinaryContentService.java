package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    void create(CreateBinaryContentRequest binaryContentDto);

    BinaryContent find(UUID id);

    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

    void delete(UUID contentId);
}
