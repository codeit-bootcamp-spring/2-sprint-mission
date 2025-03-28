package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

public interface FileStorageService {
    BinaryContentCreateRequest uploadFile(MultipartFile file, UUID fileId);
    byte[] readFile(Path path);
    String getExtension(String originalFileName);
    void deleteFile(UUID fileId);
}
