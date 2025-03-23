package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDTO;
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
    public Message createMessage(CreateMessageDTO dto) {
        if (!userRepository.existsById(dto.userId())) {
            throw new NoSuchElementException("존재하지 않는 사용자 입니다. 메세지를 생성할 수 없습니다.");
        }
        if (!channelRepository.existsById(dto.channelId())) {
            throw new NoSuchElementException("존재하지 않는 채널 입니다. 메세지를 생성할 수 없습니다.");
        }
        Message message = new Message(dto.text(), dto.userId(), dto.channelId(), dto.attachmentIds());

        return messageRepository.save(message);
    }

    @Override
    public Message searchMessage(UUID messageId) {
        return getMessage(messageId);
    }

    @Override
    public List<Message> searchAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public Message updateMessage(UpdateMessageDTO dto) {
        Message message = getMessage(dto.messageId());
        message.updateText(dto.text());
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);

        List<UUID> attachmentIds = message.getAttachmentIds();
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            binaryContentRepository.deleteAllById(attachmentIds); // 첨부파일 삭제 (배치 삭제)
        }
        messageRepository.delete(messageId);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + messageId + "인 메세지를 찾을 수 없습니다."));
    }
}
