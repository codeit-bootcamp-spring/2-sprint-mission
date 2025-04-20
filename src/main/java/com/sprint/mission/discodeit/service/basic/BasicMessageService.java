package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.controller.PageResponse;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.service.message.MessageDto;
import com.sprint.mission.discodeit.dto.service.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BasicBinaryContentService basicBinaryContentService;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest createRequest,
      List<BinaryContentCreateRequest> binaryRequestList) {
    UUID channelId = createRequest.channelId();
    UUID authorId = createRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 에 해당하는 Channel를 찾을 수 없음"));
    User author = userRepository.findByIdWithProfileAndUserStatus(authorId)
        .orElseThrow(() -> new NoSuchElementException(authorId + " 에 해당하는 Author를 찾을 수 없음"));

    List<BinaryContent> attachmentList = binaryRequestList.stream()
        .map(basicBinaryContentService::create).toList();

    Message message = new Message(createRequest.content(), channel, author, attachmentList);
    messageRepository.save(message);

    return messageMapper.toDto(message);
  }

  @Override
  public Message find(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 에 해당하는 Message를 찾을 수 없음"));
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {
    Slice<MessageDto> messageDtoSlice = messageRepository
        .findAllByChannelIdWithAuthor(
            channelId, Optional.ofNullable(cursor).orElse(Instant.now()), pageable)
        .map(messageMapper::toDto);

    Instant nextCursor = null;
    if(!messageDtoSlice.isEmpty()){
      nextCursor = messageDtoSlice.getContent().get(messageDtoSlice.getContent().size() - 1).createdAt();
    }

    return pageResponseMapper.fromSlice(messageDtoSlice, nextCursor);
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateRequest updateRequest) {
    Message message = messageRepository.findByIdWithAuthorAndAttachments(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 Message를 찾을 수 없음"));
    message.update(updateRequest.newContent());

    return messageMapper.toDto(message);
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
