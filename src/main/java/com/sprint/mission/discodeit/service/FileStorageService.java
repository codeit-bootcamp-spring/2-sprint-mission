package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {
    public BinaryContentCreateRequest uploadFile(MultipartFile file, UUID fileId);
    public String getExtension(String originalFileName);
    public void deleteFile(UUID fileId);
}
