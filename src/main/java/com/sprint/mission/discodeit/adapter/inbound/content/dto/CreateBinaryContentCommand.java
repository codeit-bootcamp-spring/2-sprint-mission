package com.sprint.mission.discodeit.adapter.inbound.content.dto;

public record CreateBinaryContentCommand(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
