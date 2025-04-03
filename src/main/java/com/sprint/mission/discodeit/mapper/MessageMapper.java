package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  CreateMessageParam toMessageParam(CreateMessageRequestDTO createMessageRequestDTO);

  default CreateMessageResponseDTO toMessageResponseDTO(MessageDTO messageDTO) {
    return new CreateMessageResponseDTO(
        messageDTO.id(),
        messageDTO.createdAt(),
        messageDTO.attachmentIds(),
        messageDTO.content(),
        messageDTO.channelId(),
        messageDTO.authorId());
  }

  UpdateMessageParam toUpdateMessageParam(UpdateMessageRequestDTO updateMessageRequestDTO);

  UpdateMessageResponseDTO toUpdateMessageResponseDTO(UpdateMessageDTO updateMessageDTO);

  default Message toEntity(CreateMessageParam createMessageParam,
      List<BinaryContent> binaryContentList) {
    return Message.builder()
        .attachmentIds(!binaryContentList.isEmpty() ? binaryContentList.stream()
            .map(BinaryContent::getId).toList() : null)
        .authorId(createMessageParam.authorId())
        .channelId(createMessageParam.channelId())
        .content(createMessageParam.content())
        .build();
  }

  @Mapping(source = "message.id", target = "id")
  @Mapping(source = "message.createdAt", target = "createdAt")
  @Mapping(source = "message.updatedAt", target = "updatedAt")
  MessageDTO toMessageDTO(Message message, UserDTO userDTO);

  @Mapping(source = "message.id", target = "id")
  @Mapping(source = "message.updatedAt", target = "updatedAt")
  UpdateMessageDTO toUpdateMessageDTO(Message message, UserDTO userDTO);

}
