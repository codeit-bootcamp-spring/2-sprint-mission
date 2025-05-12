package com.sprint.mission.discodeit.core.content.controller;

import com.sprint.mission.discodeit.core.content.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;

public final class BinaryContentDtoMapper {

  private BinaryContentDtoMapper() {

  }

  public static BinaryContentDto toCreateResponse(BinaryContent binaryContent) {
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }

  public static BinaryContentDto toCreateResponse(BinaryContentResult result) {
    return new BinaryContentDto(result.id(), result.fileName(), result.size(),
        result.contentType());
  }

}
