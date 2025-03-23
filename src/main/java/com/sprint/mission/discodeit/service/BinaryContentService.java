package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.BinaryContentDto;
import com.sprint.mission.discodeit.application.BinaryContentsDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    UUID createProfileImage(MultipartFile multipartFile);

    BinaryContentDto findById(UUID id);

    BinaryContentsDto findByIdIn(List<UUID> ids);

    void delete(UUID id);
}
