package com.sprint.mission.discodeit.domain.binarycontent.event;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;

public record BinaryContentUploadStatusChangedEvent(
    BinaryContentResult binaryContentResult
) {

  public static BinaryContentUploadStatusChangedEvent from(BinaryContent binaryContent) {
    return new BinaryContentUploadStatusChangedEvent(BinaryContentResult.fromEntity(binaryContent));
  }

}
