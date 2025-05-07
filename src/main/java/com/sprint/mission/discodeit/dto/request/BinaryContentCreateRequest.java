package com.sprint.mission.discodeit.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    MultipartFile file
) {

}
