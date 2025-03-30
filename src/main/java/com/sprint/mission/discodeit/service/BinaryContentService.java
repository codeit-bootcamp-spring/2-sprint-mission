package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    UUID createProfileImage(MultipartFile multipartFile);

    BinaryContentResult findById(UUID id);

    List<BinaryContentResult> findByIdIn(List<UUID> ids);

    void delete(UUID id);
}
