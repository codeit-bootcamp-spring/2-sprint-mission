package com.sprint.mission.discodeit.core.content.usecase.dto;

public record CreateBinaryContentCommand(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
