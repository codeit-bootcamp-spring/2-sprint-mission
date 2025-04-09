package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public BinaryContent convertCreateRequestToBinaryContent() {
    return new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
  }

}
