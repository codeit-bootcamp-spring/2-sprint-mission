package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.message.CreateMessageParam;
import com.sprint.mission.discodeit.dto.service.message.MessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageDTO;
import com.sprint.mission.discodeit.dto.service.message.UpdateMessageParam;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
    private final ChannelRepository channelRepository;
    private final UserStatusService userStatusService;
    private final UserRepository userRepository;
    private final BinaryContentService binaryContentService;
    private final UserMapper userMapper;


    @Override
    public MessageDTO create(CreateMessageParam createMessageParam, List<MultipartFile> multipartFiles) {
        // 유저와 채널이 실제로 존재하는지 검증
        findUserById(createMessageParam.authorId());
        findChannelById(createMessageParam.channelId());

        List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);
        if(binaryContentList.get(0).getSize() != 0) {
            binaryContentList.forEach(binaryContentService::create);
        }

        Message message = createMessageEntity(createMessageParam, binaryContentList);
        messageRepository.save(message);

        UserDTO userDTO = createUserDTO(message.getAuthorId());
        return entityToDTO(message, userDTO);
    }

    @Override
    public MessageDTO find(UUID messageId) {
        Message message = findMessageById(messageId);
        UserDTO userDTO = createUserDTO(message.getAuthorId());
        return entityToDTO(message, userDTO);
    }

    @Override
    public List<MessageDTO> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(message -> entityToDTO(message, createUserDTO(message.getAuthorId())))
                .toList();
    }

    @Override
    public UpdateMessageDTO update(UUID id, UpdateMessageParam updateMessageParam, List<MultipartFile> multipartFiles) {
        // 유저와 채널이 실제로 존재하는지 검증
        Message message = findMessageById(id);
        message.updateMessageInfo(updateMessageParam.content());
        replaceMessageAttachments(message, multipartFiles);
        Message updatedMessage = messageRepository.save(message);
        UserDTO userDTO = createUserDTO(updatedMessage.getAuthorId());
        return entityToMessageDTO(updatedMessage, userDTO);
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
        // 디버깅 했을 때, multipartFile이 없어도 filename ="", size=0으로 들어오는 것을 발견하여, 이 경우 처리 X
        // 들어온 multipartFile이 없다면, 처리가 안될 것이고, 기존에 있던 이미지 유지
        if(multipartFiles.get(0).getSize() == 0) {
            return;
        }
        message.getAttachmentIds().forEach(binaryContentService::delete);

        List<BinaryContent> binaryContentList = createBinaryContentList(multipartFiles);
        binaryContentList.forEach(binaryContentService::create);

        List<UUID> binaryContentIdList = binaryContentList.stream()
                .map(BinaryContent::getId)
                .collect(Collectors.toList());

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
                    .attachmentIds(binaryContentList.get(0).getSize() != 0 ? binaryContentList.stream().map(BinaryContent::getId).toList() : null)
                    .authorId(createMessageParam.authorId())
                    .channelId(createMessageParam.channelId())
                    .content(createMessageParam.content())
                    .build();
        }

        private UserDTO createUserDTO(UUID userId) {
            User user = findUserById(userId);
            UserStatus userStatus = userStatusService.findByUserId(user.getId());
            BinaryContentDTO binaryContentDTO = user.getProfileId() != null ? binaryContentService.find(user.getProfileId()) : null;
            return userMapper.toUserDTO(user, userStatus, binaryContentDTO);
        }


        private MessageDTO entityToDTO (Message message, UserDTO userDTO){
            return new MessageDTO(message.getId(), message.getCreatedAt(), message.getUpdatedAt(), message.getAttachmentIds(), message.getContent(), message.getChannelId(), userDTO);
        }

        private Message findMessageById (UUID id){
            return messageRepository.findById(id)
                    .orElseThrow(() -> RestExceptions.MESSAGE_NOT_FOUND);
        }

        private UpdateMessageDTO entityToMessageDTO(Message message, UserDTO userDTO) {
        return new UpdateMessageDTO(message.getId(), message.getUpdatedAt(), message.getAttachmentIds(), message.getContent(), message.getChannelId(), userDTO);
        }

        private User findUserById(UUID userId) {
            return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
        }

        private Channel findChannelById(UUID channelId) {
            return channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
        }
    }
