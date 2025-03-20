package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface BinaryContentService {
    UUID createProfileImage(MultipartFile multipartFile);

    BinaryContent findById(UUID id);

    void delete(UUID id);
}
