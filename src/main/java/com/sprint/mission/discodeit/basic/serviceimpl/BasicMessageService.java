package com.sprint.mission.discodeit.basic.serviceimpl; // 서비스 구현체 패키지

import com.sprint.mission.discodeit.util.UpdateOperation;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ForbiddenException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.MessageMapping;

import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;

  @Override
  public MessageDto.Response create(MessageDto.Create messageCreateDTO, UUID authorId) {
    validateUserInChannel(messageCreateDTO.getChannelId(), authorId);

    Message message = new Message(
        messageCreateDTO.getChannelId(),
        authorId,
        messageCreateDTO.getMessage()
    );
    if (messageCreateDTO.getBinaryContents() != null && !messageCreateDTO.getBinaryContents()
        .isEmpty()) {
      validateAttachmentIdsExist(messageCreateDTO.getBinaryContents());
      for (UUID fileId : messageCreateDTO.getBinaryContents()) {
        message.addAttachment(fileId);
      }
    }

    if (!messageRepository.register(message)) {
      throw new RuntimeException("메시지 저장에 실패했습니다.");
    }
    return MessageMapping.INSTANCE.messageToResponse(message);


  }


  private void validateUserInChannel(UUID channelId, UUID userId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId));
    User user = userRepository.findByUser(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

    boolean userInChannelList = channel.getUserList().contains(userId);
    boolean channelInUserBelongs = user.getBelongChannels().contains(channelId);

    if (!(userInChannelList && channelInUserBelongs)) {
      throw new ForbiddenException("존재하지 않음");
    }
  }

  private void validateAttachmentIdsExist(List<UUID> attachmentIds) {
    if (attachmentIds == null || attachmentIds.isEmpty()) {
      return;
    }
    List<BinaryContentDto.Summary> existingSummaries = binaryContentService.findBinaryContentSummariesByIds(
        attachmentIds);

    if (existingSummaries.size() != attachmentIds.size()) {
      List<UUID> existingIds = existingSummaries.stream()
          .map(summary -> UUID.fromString(String.valueOf(summary.getId())))
          .toList();
      List<UUID> nonExistentIds = new ArrayList<>(attachmentIds);
      nonExistentIds.removeAll(existingIds);
      throw new InvalidRequestException("attachments",
          "존재하지 않는 첨부파일 ID가 포함되어 있습니다: " + nonExistentIds);
    }
  }

  @Override
  public MessageDto.Response findByMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId)); // 404

    return MessageMapping.INSTANCE.messageToResponse(message);
  }

  @Override
  public List<MessageDto.Response> findAllMessage() {
    return messageRepository.findAll().stream()
        .map(MessageMapping.INSTANCE::messageToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel", "id", channelId)); // 404

    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    return messages.stream()
        .map(MessageMapping.INSTANCE::messageToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public MessageDto.Response updateMessage(UUID messageId, MessageDto.Update messageUpdateDTO,
      UUID uuid) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

    if (messageUpdateDTO.getMessage() != null && !messageUpdateDTO.getMessage()
        .equals(message.getMessage())) {
      message.updateMessage(messageUpdateDTO.getMessage());
    }
    List<UUID> binaryContentIds = messageUpdateDTO.getBinaryContents();
    UpdateOperation operation = messageUpdateDTO.getOperation();

    if (operation != null && binaryContentIds != null && !binaryContentIds.isEmpty()) {

      switch (operation) {
        case add:
          validateAttachmentIdsExist(binaryContentIds);
          message.getAttachmentIds().addAll(binaryContentIds);
          break;
        case remove:
          List<UUID> deleteList = new ArrayList<>(binaryContentIds);
          deleteList.retainAll(message.getAttachmentIds());

          if (!deleteList.isEmpty()) {
            binaryContentService.deleteBinaryContentsByIds(deleteList);
            deleteList.forEach(message.getAttachmentIds()::remove);
          }
          break;
        default:
          throw new InvalidRequestException("잘못된 요청");
      }
    }
    message.setUpdateAt();

    return MessageMapping.INSTANCE.messageToResponse(message);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));
    if (message.getAttachmentIds().isEmpty()) {
      messageRepository.deleteMessage(messageId);
      return;
    }
    messageRepository.deleteMessage(messageId);
    message.setUpdateAt();
    List<UUID> attachmentIds = message.getAttachmentIds().stream().toList();
    for (UUID attachmentId : attachmentIds) {
      binaryContentService.deleteBinaryContentByOwner(attachmentId);
    }

  }
}