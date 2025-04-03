package com.sprint.mission.discodeit.dto.service.binarycontent;

public record CreateBinaryContentParam(
    String fileName,
    String contentType,
    byte[] bytes
) {

}
