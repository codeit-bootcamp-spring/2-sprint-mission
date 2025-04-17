package com.sprint.mission.discodeit.dto.service.binarycontent;

public record CreateBinaryContentCommand(
    String filename,
    long size,
    String contentType,
    byte[] bytes
) {

}
