package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryRequestList) {
    UUID channelId = createRequest.channelId();
    UUID authorId = createRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel를 찾을 수 없음"));
    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException(authorId + " 에 해당하는 Author를 찾을 수 없음"));

    List<BinaryContent> attachmentList = binaryRequestList.stream()
        .map(basicBinaryContentService::create).toList();

    Message message = new Message(createRequest.content(), channel, author, attachmentList);
    messageRepository.save(message);

    List<BinaryContentDto> contentDtoList = attachmentList.stream()
        .map(BinaryContentDto::of).toList();

    BinaryContentDto profileDto = BinaryContentDto.of(author.getProfile());
    UserDto userDto = UserDto.of(author, profileDto, author.getUserStatus().isOnline());
    return MessageDto.of(message, userDto, contentDtoList);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 에 해당하는 Message를 찾을 수 없음"));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    return messages.stream().map(message -> {
      List<BinaryContentDto> contentDtoList = message.getAttachments().stream()
          .map(BinaryContentDto::of).toList();
      User author = message.getUser();
      BinaryContentDto profileDto = BinaryContentDto.of(author.getProfile());
      UserDto userDto = UserDto.of(author, profileDto, author.getUserStatus().isOnline());
      return MessageDto.of(message, userDto, contentDtoList);
    }).toList();
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateRequest updateRequest) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Message를 찾을 수 없음"));
    message.update(updateRequest.newContent());
    messageRepository.save(message);
    User author = message.getUser();
    List<BinaryContentDto> contentDtoList = message.getAttachments().stream()
        .map(BinaryContentDto::of).toList();
    BinaryContentDto profileDto = BinaryContentDto.of(author.getProfile());
    UserDto userDto = UserDto.of(author, profileDto, author.getUserStatus().isOnline());
    return MessageDto.of(message, userDto, contentDtoList);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException(messageId + " 에 해당하는 Message를 찾을 수 없음");
    }
    messageRepository.deleteById(messageId);
  }
}
