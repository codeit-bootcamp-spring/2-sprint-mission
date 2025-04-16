package com.sprint.mission.discodeit.core.message.usecase;

import static com.sprint.mission.discodeit.exception.message.MessageErrors.messageIdNotFoundError;

import com.sprint.mission.discodeit.core.BaseEntity;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.channel.ChannelErrors;
import com.sprint.mission.discodeit.exception.user.UserErrors;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

  private final UserRepositoryPort userRepository;
  private final ChannelRepositoryPort channelRepository;
  private final MessageRepositoryPort messageRepository;

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentMetaRepositoryPort binaryContentRepository;

  @Override
  @Transactional
  public MessageResult create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands) {

    User user = userRepository.findById(command.authorId()).orElseThrow(
        () -> UserErrors.userIdNotFoundError(command.authorId())
    );

    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> ChannelErrors.channelIdNotFoundError(command.channelId())
    );

    List<BinaryContent> binaryContentIdList = binaryContentCommands.stream()
        .map(createBinaryContentCommand -> {
          String fileName = createBinaryContentCommand.fileName();
          String contentType = createBinaryContentCommand.contentType();
          byte[] bytes = createBinaryContentCommand.bytes();

          BinaryContent binaryContent = BinaryContent.create(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        }).toList();

    Message message = Message.create(user, channel, command.content(),
        binaryContentIdList);

    Message save = messageRepository.save(message);

    logger.info(
        "Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}, attachments {}",
        message.getId(), channel.getId(), user.getId(), message.getContent(),
        message.getAttachment());

    return MessageResult.create(save, user);
  }

//  @Override
//  public MessageResult find(UUID messageId, Pageable pageable) {
//    Slice<Message> messages = messageRepository.findById(messageId, pageable);
//    return MessageResult.create(message, message.getAuthor());
//  }

  @Override
  @Transactional(readOnly = true)
  public Slice<MessageResult> findMessagesByChannelId(UUID channelId, Pageable pageable) {
    Slice<Message> messageSlice = messageRepository.findAllByChannelId(channelId, pageable);

    return messageSlice.map(message -> MessageResult.create(message, message.getAuthor()));
  }

  @Override
  @Transactional
  public MessageResult update(UpdateMessageCommand command) {
    Message message = messageRepository.findById(command.messageId())
        .orElseThrow(() -> messageIdNotFoundError(command.messageId()));

    message.update(command.newText());
    return MessageResult.create(message, message.getAuthor());
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> messageIdNotFoundError(messageId));

    messageRepository.delete(messageId);
    message.getAttachment().stream().map(BaseEntity::getId)
        .forEach(binaryContentRepository::delete);

  }
}
