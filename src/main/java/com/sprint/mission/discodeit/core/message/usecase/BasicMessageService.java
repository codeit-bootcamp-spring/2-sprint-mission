package com.sprint.mission.discodeit.core.message.usecase;

import static com.sprint.mission.discodeit.exception.message.MessageErrors.messageIdNotFoundError;

import com.sprint.mission.discodeit.core.base.BaseEntity;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepositoryPort;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.CreateMessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageListResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.MessageResult;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.dto.UpdateMessageResult;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.channel.ChannelErrors;
import com.sprint.mission.discodeit.exception.user.UserErrors;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

  private final UserRepositoryPort userRepository;
  private final ChannelRepositoryPort channelRepository;
  private final MessageRepositoryPort messageRepository;
  private final BinaryContentRepositoryPort binaryContentRepository;

  @Override
  public CreateMessageResult create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands) {

    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> UserErrors.userIdNotFoundError(command.userId())
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
              contentType, bytes);

          return binaryContentRepository.save(binaryContent);
        }).toList();

    Message message = Message.create(user, channel, command.text(),
        binaryContentIdList);

    Message save = messageRepository.save(message);

    logger.info(
        "Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}, attachment {}",
        message.getId(), channel.getId(), user.getId(), message.getContent(),
        message.getAttachmentIds());

    return new CreateMessageResult(save);
  }

  @Override
  public MessageResult findMessageByMessageId(UUID messageId) {
    return MessageResult.create(messageRepository.findById(messageId)
        .orElseThrow(() -> messageIdNotFoundError(messageId)));
  }

  @Override
  public MessageListResult findMessagesByChannelId(UUID channelId) {
    return new MessageListResult(
        messageRepository.findAllByChannelId(channelId).stream().map(MessageResult::create)
            .toList());
  }

  @Override
  public UpdateMessageResult update(UpdateMessageCommand command) {
    Message message = messageRepository.findById(command.messageId())
        .orElseThrow(() -> messageIdNotFoundError(command.messageId()));

    message.update(command.newText());
    return new UpdateMessageResult(message);
  }

  @Override
  public void delete(UUID messageId) {

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> messageIdNotFoundError(messageId));

    messageRepository.delete(messageId);
    message.getAttachmentIds().stream().map(BaseEntity::getId)
        .forEach(binaryContentRepository::delete);

  }
}
