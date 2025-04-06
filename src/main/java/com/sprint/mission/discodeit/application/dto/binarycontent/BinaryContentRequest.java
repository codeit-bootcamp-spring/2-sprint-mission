package com.sprint.mission.discodeit.application.dto.binarycontent;

import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;

public record BinaryContentRequest(String fileName, String contentType, byte[] bytes) {
    public static BinaryContentRequest fromMultipartFile(MultipartFile multipartFile) {
        return new BinaryContentRequest(multipartFile.getName(), multipartFile.getContentType(), getBytesFromMultiPartFile(multipartFile));
    }
}
