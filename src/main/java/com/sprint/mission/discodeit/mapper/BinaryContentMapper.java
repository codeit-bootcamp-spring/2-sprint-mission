package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentResponse toResponse(BinaryContent content) {
    return new BinaryContentResponse(
        content.getId(),
        content.getFileName(),
        content.getSize(),
        content.getContentType(),
        content.getBytes()
    );
  }
}