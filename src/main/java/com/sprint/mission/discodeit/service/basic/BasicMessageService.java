package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;


    @Override
    public MessageDTO create(CreateMessageParam createMessageParam, List<MultipartFile> multipartFiles) {
        List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);
        if(!binaryContentList.isEmpty()) {
            binaryContentList.forEach(binaryContentService::create);
        }
        Message message = createMessageEntity(createMessageParam, binaryContentList);
        messageRepository.save(message);
        return entityToDTO(message);
    }

    @Override
    public MessageDTO find(UUID messageId) {
        Message message = findMessageById(messageId);
        return entityToDTO(message);
    }

    @Override
    public List<MessageDTO> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(message -> entityToDTO(message))
                .toList();
    }

    @Override
    public UpdateMessageDTO update(UUID id, UpdateMessageParam updateMessageParam, List<MultipartFile> multipartFiles) {
        Message message = findMessageById(id);
        message.updateMessageInfo(updateMessageParam.content());
        if(!multipartFiles.isEmpty()) {
            replaceMessageAttachments(message, multipartFiles);
        }
        Message updatedMessage = messageRepository.save(message);
        return entityToMessageDTO(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = findMessageById(messageId);
        if (message.getAttachmentIds() != null && !message.getAttachmentIds().isEmpty()) {
            message.getAttachmentIds()
                    .forEach(binaryContentService::delete);
        }
        messageRepository.deleteById(messageId);
    }

    // multipartFiles 있을 시 원래 있던 binaryContent 삭제 후 수정 내용으로 재생성
    private void replaceMessageAttachments(Message message, List<MultipartFile> multipartFiles) {
        message.getAttachmentIds().forEach(binaryContentService::delete);

        List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);
        binaryContentList.forEach(binaryContentService::create);

        List<UUID> binaryContentIdList = binaryContentList.stream()
                .map(BinaryContent::getId)
                .toList();

        message.updateMessageAttachment(binaryContentIdList);
    }

    private List<BinaryContent> createBinaryContentList(List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }
        return multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return BinaryContent.builder()
                                .contentType(multipartFile.getContentType())
                                .bytes(multipartFile.getBytes()) // IOException 처리됨
                                .size(multipartFile.getSize())
                                .filename(multipartFile.getOriginalFilename())
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("파일 읽기 오류: " + multipartFile.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }


        private Message createMessageEntity (CreateMessageParam createMessageParam, List<BinaryContent> binaryContentList){
            return Message.builder()
                    .attachmentIds(binaryContentList.stream().map(BinaryContent::getId).toList())
                    .authorId(createMessageParam.authorId())
                    .channelId(createMessageParam.channelId())
                    .content(createMessageParam.content())
                    .build();
        }

        private MessageDTO entityToDTO (Message message){
            return new MessageDTO(message.getId(), message.getCreatedAt(), message.getUpdatedAt(), message.getAttachmentIds(), message.getContent(), message.getChannelId(), message.getAuthorId());
        }

        private Message findMessageById (UUID id){
            return messageRepository.findById(id)
                    .orElseThrow(() -> RestExceptions.MESSAGE_NOT_FOUND);
        }

        private UpdateMessageDTO entityToMessageDTO(Message message) {
        return new UpdateMessageDTO(message.getId(), message.getUpdatedAt(), message.getAttachmentIds(), message.getContent(), message.getChannelId(), message.getAuthorId());
        }
    }
