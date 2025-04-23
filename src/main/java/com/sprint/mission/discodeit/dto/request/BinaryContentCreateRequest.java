package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.domain.BinaryContent;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes // 삭제 필요
) {

  public BinaryContent convertCreateRequestToBinaryContent() {
    return BinaryContent.create(fileName, (long) bytes.length, contentType);
  }

}
