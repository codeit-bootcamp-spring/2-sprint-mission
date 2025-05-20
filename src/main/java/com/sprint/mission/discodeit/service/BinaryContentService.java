package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto create(MultipartFile file);

    BinaryContent find(UUID binaryContentId);

    List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

    void delete(UUID binaryContentId);
}
