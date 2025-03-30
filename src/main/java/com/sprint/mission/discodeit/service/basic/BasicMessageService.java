package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;


    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentDTO> binaryContentDtos) {
        UUID channelId = messageCreateRequest.channelId();
        UUID userId = messageCreateRequest.userId();
        if(channelRepository.findById(channelId) == null){
            throw new NoSuchElementException("채널 " + channelId + " 를 찾을 수 없습니다.");
        }
        if(userRepository.findById(userId) == null){
            throw new NoSuchElementException("유저 " + userId + " 를 찾을 수 없습니다.");
        }

        List<UUID> attachmentIds = binaryContentDtos.stream()
                .map(attachmentRequest -> {
                   BinaryContentType contentType = attachmentRequest.type();
                   byte[] content = attachmentRequest.content();

                    BinaryContent binaryContent = new BinaryContent(contentType,content);
                    BinaryContent createBinaryContent =  binaryContentRepository.save(binaryContent);
                    return createBinaryContent.getId();
                })
                .toList();

        String message = messageCreateRequest.message();
        Message newMessage = new Message(message,userId,channelId,attachmentIds);

        return messageRepository.save(newMessage);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        return messageRepository.findByUser(userId);
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        return messageRepository.findByChannel(channelId);
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        return  messageRepository.findByUserAndByChannel(userId, channelId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id,MessageUpdateRequest request) {
        return messageRepository.update(id, request.newMessage());
    }

    @Override
    public void delete(UUID messageId) {
        if(messageRepository.findById(messageId).getAttachmentIds()!=null) {
            messageRepository.findById(messageId).getAttachmentIds().forEach(binaryContentRepository::delete);
        }
        messageRepository.delete(messageId);

    }
}
