package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.content.BinaryContentDtoMapper;
import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.message.response.MessageResponse;
import com.sprint.mission.discodeit.adapter.inbound.user.UserDtoMapper;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDtoMapper.class, BinaryContentDtoMapper.class})
public interface MessageDtoMapper {

  MessageResponse toCreateResponse(MessageResult message);

  CreateMessageCommand toCreateMessageCommand(MessageCreateRequest requestBody);

  UpdateMessageCommand toUpdateMessageCommand(UUID messageId, MessageUpdateRequest requestBody);
}
