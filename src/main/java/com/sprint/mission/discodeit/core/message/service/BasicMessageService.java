package com.sprint.mission.discodeit.core.message.service;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.message.dto.MessageDto;
import com.sprint.mission.discodeit.core.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.core.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.core.message.entity.Message;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.service.BinaryContentService;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> binaryContentCommands) {
    User user = userRepository.findByUserId(request.authorId());
    Channel channel = channelRepository.findByChannelId(request.channelId());

    List<BinaryContent> binaryContentIdList = binaryContentCommands.stream()
        .map(binaryContentService::create).toList();

    Message message = Message.create(user, channel, request.content(), binaryContentIdList);
    channel.setLastMessageAt(Instant.now());
    messageRepository.save(message);

    log.info(
        "[MessageService] Message Created: Message Id {}, Channel Id {}, Author Id {}, content {}, attachments {}",
        message.getId(), channel.getId(), user.getId(), message.getContent(),
        message.getAttachment());

    return MessageDto.from(message);
  }


  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findByMessageId(messageId);
    message.update(request.newText());
    messageRepository.save(message);

    log.info("[MessageService] Message Updated: Message Id {}, New Text {}", messageId,
        request.newText());
    return MessageDto.from(message);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findByMessageId(messageId);

    message.getAttachment()
        .forEach(binaryContent -> binaryContentService.delete(binaryContent.getId()));

    messageRepository.delete(message);
    log.info("[MessageService] message deleted successfully : id {}", messageId);
  }
}
