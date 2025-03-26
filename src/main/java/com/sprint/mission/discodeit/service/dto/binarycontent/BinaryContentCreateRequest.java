package com.sprint.mission.discodeit.service.dto.binarycontent;

import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
        MultipartFile file
) {
}