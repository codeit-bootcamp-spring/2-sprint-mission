package com.sprint.mission.discodeit.domain.binarycontent.event;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record BinaryContentCreatedEvent(

    UUID binaryContentId,
    byte[] bytes

) {

  public static BinaryContentCreatedEvent createBinaryContentCreatedEvent(
      BinaryContent binaryContent, BinaryContentRequest binaryContentRequest
  ) {
    return new BinaryContentCreatedEvent(binaryContent.getId(), binaryContentRequest.bytes());
  }

  public static List<BinaryContentCreatedEvent> createBinaryContentsCreatedEvent(
      Map<BinaryContent, BinaryContentRequest> binaryContents
  ) {
    return binaryContents.entrySet()
        .stream()
        .map(binaryContent -> new BinaryContentCreatedEvent(binaryContent.getKey().getId(),
            binaryContent.getValue().bytes()))
        .toList();
  }

}
