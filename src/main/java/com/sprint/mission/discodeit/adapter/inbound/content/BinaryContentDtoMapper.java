package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;

public final class BinaryContentDtoMapper {

  private BinaryContentDtoMapper() {

  }

  public static BinaryContentResponse toCreateResponse(BinaryContent binaryContent) {
    return new BinaryContentResponse(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }

  public static BinaryContentResponse toCreateResponse(BinaryContentResult result) {
    return new BinaryContentResponse(result.id(), result.fileName(), result.size(),
        result.contentType());
  }

}
