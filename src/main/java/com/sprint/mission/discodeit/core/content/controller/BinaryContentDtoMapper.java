package com.sprint.mission.discodeit.core.content.controller;

import com.sprint.mission.discodeit.core.content.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;

public final class BinaryContentDtoMapper {

  private BinaryContentDtoMapper() {

  }

  public static BinaryContentDto toCreateResponse(BinaryContent binaryContent) {
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }


}
