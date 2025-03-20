package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.BinaryContentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BinaryContentService {
    UUID createProfileImage(MultipartFile multipartFile);

    BinaryContentDto findById(UUID id);

    void delete(UUID id);
}
