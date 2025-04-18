package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public Message create(
            MessageCreateRequest messageCreateRequest,
            List<BinaryContentCreateRequest> binaryContentCreateRequests
    ) {
        String content = messageCreateRequest.content();

        UUID channelId = messageCreateRequest.channelId();
        UUID userId = messageCreateRequest.authorId();

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

        List<BinaryContent> attachments = binaryContentCreateRequests.stream()
                .map(request -> new BinaryContent(
                        request.fileName(),
                        (long) request.bytes().length,
                        request.contentType(),
                        request.bytes()
                ))
                .map(binaryContentRepository::save)
                .toList();

        Message message = new Message(
                content,
                channel,
                author,
                attachments
        );
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    @Override
    public Message searchMessage(UUID messageId) {
        return getMessage(messageId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Transactional
    @Override
    public Message updateMessage(UUID messageId, MessageUpdateRequest dto) {
        String newContent = dto.newContent();
        Message message = getMessage(messageId);
        message.updateContent(newContent);
        return message;
    }

    @Transactional
    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);

        List<BinaryContent> attachments = message.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            binaryContentRepository.deleteAll(attachments);
        }

        messageRepository.delete(message);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + messageId + "인 메세지를 찾을 수 없습니다."));
    }
}
