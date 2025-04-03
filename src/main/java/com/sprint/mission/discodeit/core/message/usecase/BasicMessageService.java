package com.sprint.mission.discodeit.core.message.usecase;

import static com.sprint.mission.discodeit.exception.message.MessageErrors.messageIdNotFoundError;

import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
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
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.channel.ChannelErrors;
import com.sprint.mission.discodeit.exception.user.UserErrors;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

  private final UserRepositoryPort userRepositoryPort;
  private final ChannelRepository channelRepository;
  private final MessageRepositoryPort messageRepositoryPort;
  private final BinaryContentRepositoryPort binaryContentRepositoryPort;

  @Override
  public CreateMessageResult create(CreateMessageCommand command,
      List<CreateBinaryContentCommand> binaryContentCommands) {
    if (!userRepositoryPort.existId(command.userId())) {
      UserErrors.userIdNotFoundError(command.userId());
    }

    if (!channelRepository.existId(command.channelId())) {
      ChannelErrors.channelIdNotFoundError(command.channelId());
    }

    List<UUID> binaryContentIdList = makeBinaryContent(binaryContentCommands);

    Message message = Message.create(command.userId(), command.channelId(), command.text(),
        binaryContentIdList);

    messageRepositoryPort.save(message);
    logger.info("Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}",
        message.getId(), message.getChannelId(), message.getUserId(), message.getContent());

    return new CreateMessageResult(message);
  }


  private List<UUID> makeBinaryContent(
      List<CreateBinaryContentCommand> commands) {
    return commands.stream()
        .map(this::saveBinaryContent)
        .collect(Collectors.toList());
  }

  private UUID saveBinaryContent(CreateBinaryContentCommand command) {
    BinaryContent content = BinaryContent.create(
        command.fileName(),
        command.bytes().length,
        command.contentType(),
        command.bytes()
    );
    binaryContentRepositoryPort.save(content);
    return content.getId();
  }

  @Override
  public MessageResult findMessageByMessageId(UUID messageId) {
    return MessageResult.create(messageRepositoryPort.findById(messageId)
        .orElseThrow(() -> messageIdNotFoundError(messageId)));
  }

  @Override
  public MessageListResult findMessagesByChannelId(UUID channelId) {
    return new MessageListResult(
        messageRepositoryPort.findAllByChannelId(channelId).stream().map(MessageResult::create)
            .toList());
  }

  @Override
  public UpdateMessageResult update(UpdateMessageCommand command) {
    Message message = messageRepositoryPort.findById(command.messageId())
        .orElseThrow(() -> messageIdNotFoundError(command.messageId()));

    message.update(command.newText());
    return new UpdateMessageResult(message);
  }

  @Override
  public void delete(UUID messageId) {

    Message message = messageRepositoryPort.findById(messageId)
        .orElseThrow(() -> messageIdNotFoundError(messageId));

    messageRepositoryPort.deleteByMessageId(messageId);
    message.getAttachmentIds().forEach(binaryContentRepositoryPort::delete);
  }
}
