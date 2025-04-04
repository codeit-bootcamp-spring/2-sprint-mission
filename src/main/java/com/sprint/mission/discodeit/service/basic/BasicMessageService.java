package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.FindMessageByChannelIdResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message sendMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequestList) {
    User user = userRepository.findUserById(messageCreateRequest.userId())
        .orElseThrow(() -> new NoSuchElementException(
            messageCreateRequest.userId() + "에 해당하는 사용자를 찾을 수 없습니다."));

    Channel channel = channelRepository.findChannelById(messageCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            messageCreateRequest.channelId() + "에 해당하는 채널을 찾을 수 없습니다."));

    List<UUID> attachmentList = binaryContentCreateRequestList.stream()
        .map(data -> {
          String filename = data.fileName();
          String contentType = data.contentType();
          byte[] bytes = data.bytes();
          BinaryContent binaryContent = BinaryContent.builder()
              .fileName(filename)
              .contentType(contentType)
              .bytes(bytes)
              .size((long) bytes.length)
              .build();

          binaryContentRepository.save(binaryContent);
          return binaryContent.getId();
        })
        .toList();

    Message message = new Message(
        messageCreateRequest.content(), user.getId(),
        channel.getId(), attachmentList);
    messageRepository.save(message);
    return message;
  }

  @Override
  public Message findMessageById(UUID messageId) {
    return messageRepository.findMessageById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다."));
  }

  @Override
  public List<Message> findAllMessages() {
    return messageRepository.findAllMessage();
  }

  @Override
  public List<FindMessageByChannelIdResponseDto> findMessageByChannelId(UUID channelUUID) {
    List<Message> channelMessageList = messageRepository.findMessageByChannel(channelUUID);

    return channelMessageList.stream()
        .sorted(Comparator.comparing(Message::getCreatedAt))
        .map((message) -> {
          String nickname = userRepository.findUserById(message.getUserUUID())
              .map(User::getUsername)
              .orElse("알 수 없음");

          return new FindMessageByChannelIdResponseDto(
              message.getId(), nickname,
              message.getAttachmentList(), message.getContent(),
              message.getCreatedAt()
          );
        })
        .toList();
  }

  @Override
  public Message updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findMessageById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다."));
    message.updateContent(messageUpdateRequest.content());
    messageRepository.save(message);
    return message;
  }

  @Override
  public void deleteMessageById(UUID messageId) {
    Message message = messageRepository.findMessageById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다."));
    message.getAttachmentList().forEach(binaryContentRepository::delete);
    messageRepository.delete(message.getId());
  }
}
