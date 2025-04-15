package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;

public final class BinaryContentDtoMapper {

  private BinaryContentDtoMapper() {

  }

  public static BinaryContentResponse toCreateResponse(BinaryContent binaryContent) {
    return new BinaryContentResponse(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }

}
