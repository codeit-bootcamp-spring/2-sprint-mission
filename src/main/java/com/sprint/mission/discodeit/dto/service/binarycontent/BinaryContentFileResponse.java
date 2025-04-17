package com.sprint.mission.discodeit.dto.service.binarycontent;

public record BinaryContentFileResponse(
    String contentType,
    byte[] fileBytes
) {

}
