package com.sprint.mission.discodeit.dto.binaryContent;

public record CreateBinaryContentRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}