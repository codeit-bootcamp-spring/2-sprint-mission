package com.sprint.mission.discodeit.adapter.inbound.status;

import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusResponse;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadStatusDtoMapper {

  ReadStatusResponse toCreateResponse(ReadStatusResult readStatus);

  CreateReadStatusCommand toCreateReadStatusCommand(ReadStatusCreateRequest requestBody);

  UpdateReadStatusCommand toUpdateReadStatusCommand(UUID readStatusId,
      ReadStatusUpdateRequest requestBody);

}
