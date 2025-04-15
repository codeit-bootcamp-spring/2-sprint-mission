package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentDtoMapper {

  BinaryContentResponse toCreateResponse(BinaryContentResult content);

}
