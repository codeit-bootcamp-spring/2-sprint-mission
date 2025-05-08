package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(BasicMessageService.class);

    private final MessageJPARepository messageJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelJPARepository channelJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final PageMapper pageMapper;
    private final ResponseMapStruct responseMapStruct;

    @Override
    @Transactional
    public MessageResponseDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtoList) {
        logger.debug("[Message][create] Calling userJpaRepository.findById(): authorId={}", messageCreateDto.authorId());
        User matchingUser = userJpaRepository.findById(messageCreateDto.authorId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        logger.debug("[Message][create] Calling channelJpaRepository.findById(): channelId={}", messageCreateDto.channelId());
        Channel matchingChannel = channelJpaRepository.findById(messageCreateDto.channelId())
                .orElseThrow(() -> new NotFoundException("Channel not found."));

        List<BinaryContent> attachments = binaryContentCreateDtoList.stream()
                .map(profileRequest -> {
                    logger.debug("[Message] Starting profile upload process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent createBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(createBinaryContent.getId(), bytes);
                    logger.debug("[Message] Profile upload completed process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    return createBinaryContent;
                })
                .toList();

        logger.debug("[Message][create] Entity constructed: channelID={}, authorId={}", messageCreateDto.authorId(), messageCreateDto.channelId());
        Message messages = new Message(messageCreateDto.content(), matchingChannel, matchingUser, attachments);
        logger.debug("[Message][create] Calling messageJpaRepository.save()");
        Message createdMessage = messageJpaRepository.save(messages);
        logger.info("[Message][create] Created successfully: messageId={}", createdMessage.getId());
        return responseMapStruct.toMessageDto(createdMessage);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MessageResponseDto> find(UUID messageId, int page, int size) {
        Page<MessageResponseDto> matchingMessage = messageJpaRepository
                .findByIdEntityGraph(messageId, pageRequestSortByCreatedAt(page, size))
                .map(responseMapStruct::toMessageDto);
        if (matchingMessage.isEmpty()) {
            throw new NotFoundException("Message not found.");
        }
        return pageMapper.fromPage(matchingMessage);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MessageResponseDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {
        Page<MessageResponseDto> matchingMessageAll = cursor != null
                ? messageJpaRepository.findByChannel_IdEntityGraphCursor(channelId, cursor, pageable).map(responseMapStruct::toMessageDto)
                : messageJpaRepository.findByChannel_IdEntityGraph(channelId, pageable).map(responseMapStruct::toMessageDto) ;
        return pageMapper.fromPage(matchingMessageAll);
    }


    @Override
    @Transactional
    public MessageResponseDto update(UUID messageId, MessageUpdateDto messageUpdateDto) {
        logger.debug("[Message][update] Calling messageJpaRepository.findById(): messageId={}", messageId);
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        logger.debug("[Message][update] Calling updateChannel(): messageId={}", messageId);
        matchingMessage.updateMessage(messageUpdateDto.newContent());
        logger.debug("[Message][update] Calling messageJpaRepository.save()");
        Message updateMessage = messageJpaRepository.save(matchingMessage);
        logger.info("[Message][update] Updated successfully: messageId={}", messageId);
        return responseMapStruct.toMessageDto(updateMessage);
    }


    @Override
    @Transactional
    public void delete(UUID messageId) {
        logger.debug("[Message][delete] Calling messageJpaRepository.findById(): messageId={}", messageId);
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        logger.debug("[Message][delete] Calling matchingMessage.getAttachments()");
        matchingMessage.getAttachments().stream()
                .map(BinaryContent::getId)
                .forEach(binaryContentService::delete);

        logger.debug("[Message][delete] Calling messageJpaRepository.delete(): messageId={}", messageId);
        messageJpaRepository.delete(matchingMessage);
        logger.info("[Message][delete] Deleted successfully: messageId={}", messageId);
    }

    private PageRequest pageRequestSortByCreatedAt(int page, int size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }

}
