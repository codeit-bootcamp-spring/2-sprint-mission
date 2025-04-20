package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }
    return BinaryContentDto.builder()
        .id(binaryContent.getId())
        .fileName(binaryContent.getFileName())
        .size(binaryContent.getSize())
        .contentType(binaryContent.getContentType())
        .build();
  }

  public BinaryContent toEntity(BinaryContentCreateRequest binaryContentCreateRequest) {
    if (binaryContentCreateRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent();
    binaryContent.setFileName(binaryContentCreateRequest.fileName());
    binaryContent.setContentType(binaryContentCreateRequest.contentType());

    return binaryContent;
  }
}
