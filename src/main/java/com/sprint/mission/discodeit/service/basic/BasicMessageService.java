package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.MessageMapper;
import com.sprint.mission.discodeit.Mapper.PageResponseMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataMessageAttachmentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final SpringDataMessageAttachmentRepository messageAttachmentRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
                           List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();
    String content = messageCreateRequest.content();
    int attachments = binaryContentCreateRequests.size();
    log.info("메시지 생성 요청: channelId={}, authorId={}, attachments={}, content={}",
            channelId, authorId, attachments, content);

    if (!channelRepository.existsById(channelId)) {
      log.warn("메시지 생성 실패 (채널 없음): channelId={}", channelId);
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      log.warn("메시지 생성 실패 (작성자 없음): authorId={}", authorId);
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    List<BinaryContent> binaryContents = binaryContentCreateRequests.stream()
            .map(req -> {
              BinaryContent bc = binaryContentRepository.save(
                      new BinaryContent(req.fileName(), (long) req.bytes().length, req.contentType()));
              UUID uuid = binaryContentStorage.put(bc.getId(), req.bytes());
              log.info("첨부파일 저장: id={}, uuid={}, fileName={}", bc.getId(), uuid, req.fileName());
              return bc;
            })
            .collect(Collectors.toList());

    Channel channel = channelRepository.findById(channelId).orElse(null);
    User user = userRepository.findById(authorId).orElse(null);
    Message message = messageRepository.save(new Message(content, channel, user));
    log.info("메시지 저장 완료: messageId={}", message.getId());

    binaryContents.forEach(bc -> {
      messageAttachmentRepository.save(new MessageAttachment(message, bc));
    });

    MessageDto dto = messageMapper.toDto(message);
    log.info("메시지 생성 완료: messageId={}, dto={}", message.getId(), dto);
    return dto;
  }

  @Override
  public MessageDto find(UUID messageId) {
    log.info("메시지 조회 요청: messageId={}", messageId);
    MessageDto dto = messageRepository.findById(messageId)
            .map(messageMapper::toDto)
            .orElseThrow(() -> {
              log.warn("메시지 조회 실패: messageId={} not found", messageId);
              return new NoSuchElementException("Message with id " + messageId + " not found");
            });
    log.info("메시지 조회 완료: messageId={}", messageId);
    return dto;
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
    log.info("채널 메시지 페이징 조회 요청: channelId={}, page={}, size={}",
            channelId, pageable.getPageNumber(), pageable.getPageSize());
    Page<MessageDto> page = messageRepository
            .findAllByChannelIdPaging(channelId, pageable)
            .map(messageMapper::toDto);
    PageResponse<MessageDto> response = pageResponseMapper.fromPage(page);
    log.info("채널 메시지 페이징 조회 완료: channelId={}", channelId);
    return response;
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.info("메시지 수정 요청: messageId={}, newContent={}", messageId, request.newContent());
    Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
              log.warn("메시지 수정 실패: messageId={} not found", messageId);
              return new NoSuchElementException("Message with id " + messageId + " not found");
            });
    message.update(request.newContent());
    Message updated = messageRepository.save(message);
    log.info("메시지 수정 완료: messageId={}", updated.getId());
    return messageMapper.toDto(updated);
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    log.info("메시지 삭제 요청: messageId={}", messageId);
    messageRepository.deleteById(messageId);
    log.info("메시지 삭제 완료: messageId={}", messageId);
  }
}