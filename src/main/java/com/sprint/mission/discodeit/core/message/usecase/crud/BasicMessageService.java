package com.sprint.mission.discodeit.core.message.usecase.crud;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageFindDTO;
import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.UpdateMessageDTO;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.message.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepository;
import com.sprint.mission.discodeit.core.channel.port.ChannelRepository;
import com.sprint.mission.discodeit.core.message.port.MessageRepository;
import com.sprint.mission.discodeit.core.user.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public void reset(boolean adminAuth) {
    if (adminAuth) {
      messageRepository.reset();
    }
  }

  @CustomLogging
  @Override
  public Message create(UUID userId, UUID channelId, MessageCreateRequestDTO messageWriteDTO,
      List<Optional<BinaryContentCreateRequestDTO>> binaryContentDTOs) {
    User user = userRepository.findById(userId);
    Channel channel = channelRepository.find(channelId);

    List<UUID> binaryContentIdList = makeBinaryContent(binaryContentDTOs);

    Message message = new Message(user.getId(), user.getName(), channel.getChannelId(),
        messageWriteDTO.text(), binaryContentIdList);

    messageRepository.save(message);
    return message;
  }

  @Override
  public MessageFindDTO find(UUID messageId) {
    return MessageFindDTO.create(messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다.")));
  }

  @Override
  public List<MessageFindDTO> findAllByChannelId(UUID channelId) {
    List<Message> list = messageRepository.findAllByChannelId(channelId);
    return list.stream().map(MessageFindDTO::create).toList();
  }

  @CustomLogging
  @Override
  public UUID update(UUID messageId, UpdateMessageDTO updateMessageDTO) {

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));

    Message update = messageRepository.update(message, updateMessageDTO);

    return update.getMessageId();
  }

  @CustomLogging
  @Override
  public void delete(UUID messageId) {

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));

    messageRepository.deleteById(messageId);
    message.getAttachmentIds().forEach(binaryContentRepository::delete);
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
    BinaryContent content = new BinaryContent(
        binaryContentCreateDTO.fileName(),
        binaryContentCreateDTO.bytes().length,
        binaryContentCreateDTO.contentType(),
        binaryContentCreateDTO.bytes()
    );
    binaryContentRepository.save(content);
    return content.getId();
  }

//    @Override
//    public void print(UUID channelId) {
//        Channel channel = channelRepository.find(channelId);
//        System.out.println(channel.getName());
//        List<Message> messages = messageRepository.findAllByChannelId(channel.getChannelId());
//        for (Message message : messages) {
//            System.out.println(message.getUserName() + " : " + message.getText());
//        }
//    }
}
