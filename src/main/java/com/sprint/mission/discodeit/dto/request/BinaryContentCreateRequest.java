package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BinaryContentCreateRequest {
    String fileName;
    String contentType;
    byte[] bytes;

    @Builder
    public BinaryContentCreateRequest(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
