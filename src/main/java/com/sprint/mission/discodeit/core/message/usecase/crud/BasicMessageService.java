package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageCommand;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.port.MessageRepositoryPort;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.CreateMessageCommand;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageListResult;
import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageResult;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundError;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final UserRepositoryPort userRepositoryPort;
  private final ChannelRepository channelRepository;
  private final MessageRepositoryPort messageRepositoryPort;
  private final BinaryContentRepositoryPort binaryContentRepositoryPort;

  @CustomLogging
  @Override
  public Message create(CreateMessageCommand command,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs) {
    User user = userRepositoryPort.findById(command.userId()).orElseThrow(() ->
        new UserNotFoundError("유저를 찾을 수 없습니다.")
    );

    Channel channel = channelRepository.findByChannelId(command.channelId()).orElseThrow(
        () -> new ChannelNotFoundException("채널을 찾을 수 없습니다.")
    );

    List<UUID> binaryContentIdList = makeBinaryContent(binaryContentDTOs);

    Message message = Message.create(user.getId(), channel.getChannelId(),
        command.text(), binaryContentIdList);

    messageRepositoryPort.save(message);
    return message;
  }


  private List<UUID> makeBinaryContent(
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs) {
    return binaryContentDTOs.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(this::saveBinaryContent)
        .collect(Collectors.toList());
  }

  private UUID saveBinaryContent(BinaryContentCreateRequestDTO binaryContentCreateDTO) {
    BinaryContent content = BinaryContent.create(
        binaryContentCreateDTO.fileName(),
        binaryContentCreateDTO.bytes().length,
        binaryContentCreateDTO.contentType(),
        binaryContentCreateDTO.bytes()
    );
    binaryContentRepositoryPort.save(content);
    return content.getId();
  }

  @Override
  public MessageResult findMessageByMessageId(UUID messageId) {
    return MessageResult.create(messageRepositoryPort.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다.")));
  }

  @Override
  public MessageListResult findMessagesByChannelId(UUID channelId) {
    return new MessageListResult(
        messageRepositoryPort.findAllByChannelId(channelId).stream().map(MessageResult::create)
            .toList());
  }

  @CustomLogging
  @Override
  public void update(UpdateMessageCommand command) {

    Message message = messageRepositoryPort.findById(command.messageId())
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));

    message.update(command.newText(), null);
  }

  @CustomLogging
  @Override
  public void delete(UUID messageId) {

    Message message = messageRepositoryPort.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));

    messageRepositoryPort.deleteById(messageId);
    message.getAttachmentIds().forEach(binaryContentRepositoryPort::delete);
  }
}
