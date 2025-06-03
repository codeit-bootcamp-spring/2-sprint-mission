package com.sprint.mission.discodeit.core.message.usecase;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.storage.usecase.dto.BinaryContentCreateCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageCreateCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageUpdateCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {

  private final JpaUserRepository userRepository;
  private final JpaChannelRepository channelRepository;
  private final JpaMessageRepository messageRepository;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public MessageDto create(MessageCreateCommand command,
      List<BinaryContentCreateCommand> binaryContentCommands) {
    User user = userRepository.findById(command.authorId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.authorId())
    );
    Channel channel = channelRepository.findById(command.channelId()).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, command.channelId())
    );

    List<BinaryContent> binaryContentIdList = binaryContentCommands.stream().map(
            binaryContentService::create)
        .toList();

    Message message = Message.create(user, channel, command.content(),
        binaryContentIdList);

    messageRepository.save(message);

    log.info(
        "[MessageService] Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}, attachments {}",
        message.getId(), channel.getId(), user.getId(), message.getContent(),
        message.getAttachment());

    return MessageDto.create(message, user);
  }

  @Override
  public List<MessageDto> findByChannelId(UUID channelId) {
    List<Message> messages = messageRepository.findByChannel_Id(channelId);
    return messages.stream().map(message -> MessageDto.create(message, message.getAuthor()))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(cursor).orElse(Instant.now()),
            pageable)
        .map(message -> MessageDto.create(message, message.getAuthor()));

    return slice;
  }

  @Override
  @Transactional
  public MessageDto update(MessageUpdateCommand command) {
    Message message = messageRepository.findById(command.messageId())
        .orElseThrow(
            () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, command.messageId()));

    message.update(command.newText());
    messageRepository.save(message);
    log.info("[MessageService] Message Updated: Message Id {}, New Text {}", command.messageId(),
        command.newText());
    return MessageDto.create(message, message.getAuthor());
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, messageId));
    message.getAttachment()
        .forEach(binaryContent -> binaryContentService.delete(binaryContent.getId())
        );
    messageRepository.deleteById(messageId);
    log.info("[MessageService] message deleted successfully : id {}", messageId);
  }
}
