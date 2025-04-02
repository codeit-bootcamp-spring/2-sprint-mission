package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentUploadResponse uploadSingle(MultipartFile file);
    boolean existsById(UUID id);
    BinaryContentFindResponse findById(UUID id);
    List<BinaryContentFindResponse> findAllByIdIn(List<UUID> ids);
    void deleteByID(UUID id);
}
