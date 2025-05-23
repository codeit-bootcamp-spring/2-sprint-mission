package com.sprint.mission.discodeit.core.storage.controller;

import com.sprint.mission.discodeit.core.storage.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;

public final class BinaryContentDtoMapper {

  private BinaryContentDtoMapper() {

  }

  public static BinaryContentDto toCreateResponse(BinaryContent binaryContent) {
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(), binaryContent.getContentType());
  }


}
