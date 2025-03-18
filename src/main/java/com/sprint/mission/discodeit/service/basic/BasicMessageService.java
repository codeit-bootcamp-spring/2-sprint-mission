package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.service.messageDto.MessageResponse;
import com.sprint.mission.discodeit.service.messageDto.MessageUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateRequest request) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = new Message(request.content(),request.channelId(), request.userId());
        message = messageRepository.save(message);

        // 첨부파일 저장
        if (request.attachments() != null) {
            for (String filePath : request.attachments()) {
                BinaryContent binaryContent = new BinaryContent(message.getId(),
                        filePath.getBytes(StandardCharsets.UTF_8));
                binaryContentRepository.save(binaryContent);
            }
        }

        return message;
    }

    @Override
    public MessageResponse find(UUID messageId){
        Message message = messageRepository.findById(messageId).orElseThrow();
        return new MessageResponse(message,
                binaryContentRepository.findAllById(message.getAttachmentIds())
                        .stream()
                        .map(BinaryContent::getContent)
                        .map(Objects::toString)
                        .toList());

    }

    @Override
    public List<MessageResponse> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(message -> {
                    List<String> attachments = binaryContentRepository.findById(message.getId()).
                            stream()
                            .map(BinaryContent::getContent)
                            .map(Object::toString)
                            .toList();
                    return new MessageResponse(message, attachments);
                })
                .collect(Collectors.toList());
    }

    public MessageResponse update(MessageUpdateRequest request) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.update(request.content());
        messageRepository.save(message);
        return new MessageResponse(message, binaryContentRepository.findAllById(message.getAttachmentIds())
                .stream()
                .map(BinaryContent::getContent)
                .map(Objects::toString)
                .toList());
    }

    public void delete(UUID messageId) {
        binaryContentRepository.deleteById(messageId);
        messageRepository.deleteById(messageId);
    }
}
