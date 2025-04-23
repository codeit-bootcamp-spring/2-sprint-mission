package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
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

    private final MessageJPARepository messageJpaRepository;
    private final UserJPARepository userJpaRepository;
    private final ChannelJPARepository channelJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageMapper pageMapper;
    private final ResponseMapStruct responseMapStruct;

    @Override
    @Transactional
    public MessageResponseDto create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtoList) {
        User matchingUser = userJpaRepository.findById(messageCreateDto.authorId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Channel matchingChannel = channelJpaRepository.findById(messageCreateDto.channelId())
                .orElseThrow(() -> new NotFoundException("Channel not found."));

        List<BinaryContent> attachments = binaryContentCreateDtoList.stream()
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent createBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(createBinaryContent.getId(), bytes);
                    return createBinaryContent;
                })
                .toList();

        Message messages = new Message(messageCreateDto.content(), matchingChannel, matchingUser, attachments);
        messageJpaRepository.save(messages);

        return responseMapStruct.toMessageDto(messages);
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
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.updateMessage(messageUpdateDto.newContent());
        Message updateMessage = messageJpaRepository.save(matchingMessage);
        return responseMapStruct.toMessageDto(updateMessage);
    }


    @Override
    @Transactional
    public void delete(UUID messageId) {
        Message matchingMessage = messageJpaRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message does not exist."));

        matchingMessage.getAttachments().stream()
                .map(BinaryContent::getId)
                .forEach(binaryContentService::delete);

        messageJpaRepository.delete(matchingMessage);

    }

    private PageRequest pageRequestSortByCreatedAt(int page, int size) {
        return PageRequest.of(page, size, Sort.by("createdAt").descending());
    }

}
