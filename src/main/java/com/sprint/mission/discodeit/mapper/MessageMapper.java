package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.FindMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageResult;
import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageCommand;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  CreateMessageCommand toCreateMessageCommand(CreateMessageRequestDTO createMessageRequestDTO);

  CreateMessageResponseDTO toCreateMessageResponseDTO(CreateMessageResult createMessageResult);

  UpdateMessageCommand toUpdateMessageCommand(UpdateMessageRequestDTO updateMessageRequestDTO);

  UpdateMessageResponseDTO toUpdateMessageResponseDTO(UpdateMessageResult updateMessageResult);

  FindMessageResponseDTO toFindMessageResponseDTO(FindMessageResult findMessageResult);

  @Mapping(source = "channel.id", target = "channelId")
  FindMessageResult toFindMessageResult(Message message);

  @Mapping(source = "channel.id", target = "channelId")
  UpdateMessageResult toUpdateMessageResult(Message message);

  @Mapping(source = "channel.id", target = "channelId")
  CreateMessageResult toCreateMessageResult(Message message);

  FindBinaryContentResult toFindBinaryContentResult(BinaryContent binaryContent);

  @Mapping(target = "online", expression = "java(user.getUserStatus().isLoginUser())")
  FindUserResult toFindUserResult(User user);

}
