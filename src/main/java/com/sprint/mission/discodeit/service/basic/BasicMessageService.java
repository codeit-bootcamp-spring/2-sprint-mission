package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
// import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
// import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentService binaryContentService;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> attachmentRequests) {

    Message message = messageMapper.toEntity(messageCreateRequest);

    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Channel not found with id: " + messageCreateRequest.channelId()));
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Author not found with id: " + messageCreateRequest.authorId()));

    message.setChannel(channel);
    message.setAuthor(author);

    Message savedMessage = messageRepository.save(message);

    if (attachmentRequests != null && !attachmentRequests.isEmpty()) {
      List<BinaryContent> attachments = new ArrayList<>();
      for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {
        BinaryContentDto attachmentDto;
        attachmentDto = binaryContentService.create(attachmentRequest);

        BinaryContent attachmentEntity = new BinaryContent();
        attachmentEntity.setFileName(attachmentDto.fileName());
        attachmentEntity.setContentType(attachmentDto.contentType());

        attachments.add(attachmentEntity);
      }

      savedMessage.setAttachments(attachments);
      savedMessage = messageRepository.save(savedMessage);
    }

    return messageMapper.toDto(savedMessage);
  }


  @Override
  @Transactional(readOnly = true)
  public MessageDto read(UUID messageId) {
    Message message = messageRepository.findById(
            messageId)
        .orElseThrow(
            () -> new IllegalStateException("[Error] 해당 ID(" + messageId + ")의 메시지가 없습니다."));

    return messageMapper.toDto(message);
  }


  @Override
  @Transactional(readOnly = true) // 읽기 전용 트랜잭션

  public Slice<MessageDto> readAllByChannelId(UUID channelId, Pageable pageable) {

    Slice<Message> messagesSlice = messageRepository.findAllByChannel_IdOrderByCreatedAtDesc(
        channelId, pageable); // Repository 메소드 호출

    return messagesSlice.map(messageMapper::toDto);
  }


  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new IllegalArgumentException("[Error] 해당 메시지(" + messageId + ")가 존재하지 않습니다."));

    if (request.newContent() != null && !request.newContent().isEmpty()) {
      message.setText(request.newContent());
    }

    Message updatedMessage = messageRepository.save(message);

    return messageMapper.toDto(updatedMessage);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new IllegalArgumentException("[Error] 해당 메시지(" + messageId + ")가 존재하지 않습니다."));

    messageRepository.delete(message); // 메시지 본문 삭제
  }
  
}
