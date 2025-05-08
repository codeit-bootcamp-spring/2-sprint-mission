package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
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
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final PageResponseMapper pageResponseMapper;

    @Override
    @Transactional
    public MessageDto sendMessage(MessageCreateRequest messageCreateRequest,
        List<MultipartFile> attachments) {
        log.info("메세지 보내기 진행: authorId = {}, content = {}", messageCreateRequest.authorId(),
            messageCreateRequest.content());
        List<Map.Entry<BinaryContent, byte[]>> attachmentEntryList;
        List<BinaryContent> attachmentList;

        User user = userRepository.findById(messageCreateRequest.authorId())
            .orElseThrow(() -> {
                log.error("메세지 생성 중 해당하는 사용자를 찾을 수 없음: authorId = {}",
                    messageCreateRequest.authorId());
                return new NoSuchElementException(
                    messageCreateRequest.authorId() + "에 해당하는 사용자를 찾을 수 없습니다.");
            });

        Channel channel = channelRepository.findById(messageCreateRequest.channelId())
            .orElseThrow(() -> {
                log.error("메세지 생성 중 해당하는 채널 찾을 수 없음: channelId = {}",
                    messageCreateRequest.channelId());
                return new NoSuchElementException(
                    messageCreateRequest.channelId() + "에 해당하는 채널을 찾을 수 없습니다.");
            });

        attachmentEntryList = Optional.ofNullable(attachments)
            .orElse(Collections.emptyList())
            .stream()
            .filter(file -> !file.isEmpty())
            .map(file -> {
                try {
                    log.info(
                        "메세지 보내는 중 파일 메타 데이터 저장 진행: originalFileName = {}, contentType = {}, size = {}",
                        file.getOriginalFilename(), file.getContentType(), file.getSize());
                    byte[] bytes = file.getBytes();
                    BinaryContent binaryContent = BinaryContent.builder()
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size((long) bytes.length)
                        .build();
                    log.info(
                        "메세지 보내는 중 파일 메타 데이터 저장 완료: originalFileName = {}, contentType = {}, size = {}",
                        file.getOriginalFilename(), file.getContentType(), file.getSize());
                    return new AbstractMap.SimpleEntry<>(binaryContent, bytes);
                } catch (IOException e) {
                    log.error(
                        "메세지 보내는 중 파일 메타 데이터 저장 진행 중 오류 발생: originalFileName = {}, contentType = {}, size = {}",
                        file.getOriginalFilename(), file.getContentType(), file.getSize());
                    throw new RuntimeException("파일 처리 중 오류 발생", e);
                }
            })
            .collect(Collectors.toList());

        attachmentList = attachmentEntryList.stream()
            .map(Map.Entry::getKey)
            .toList();

        Message message = new Message(
            messageCreateRequest.content(), user,
            channel, attachmentList);
        messageRepository.save(message);
        log.info("메세지 생성 완료: messageId = {}", message.getId());

        attachmentEntryList.forEach(entry -> {
            log.info("메세지 생성 중 파일 실제 데이터 저장: binaryContentId = {}", entry.getKey().getId());
            binaryContentStorage.put(entry.getKey().getId(), entry.getValue());
        });

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findMessageByChannelId(UUID channelId, Pageable pageable,
        Instant cursor) {
        Slice<Message> slice;

        if (cursor != null) {
            slice = messageRepository.findSliceByChannelIdAndCreatedAtBefore(channelId, pageable,
                cursor);
        } else {
            slice = messageRepository.findSliceByChannelId(channelId, pageable);
        }

        return pageResponseMapper.fromSlice(
            slice.map(messageMapper::toDto),
            MessageDto::createdAt
        );
    }

    @Override
    @Transactional
    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.info("메세지 수정 진행: messageId = {}", messageId);
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.error("메세지 수정 중 메세지 아이디를 찾을 수 없음: messageId = {}", messageId);
                return new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다.");
            });
        message.updateContent(messageUpdateRequest.newContent());
        log.info("메세지 수정 완료: messageId = {}", messageId);
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public void deleteMessageById(UUID messageId) {
        log.info("메세지 삭제 진행: messageId = {}", messageId);
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> {
                log.error("메세지 삭제 중 메세지 아이디를 찾을 수 없음 : messageId = {}", messageId);
                return new NoSuchElementException(messageId + "에 해당하는 메세지를 찾을 수 없습니다.");
            });
        messageRepository.delete(message);
        log.info("메세지 삭제 완료: messageId = {}", messageId);
    }
}
