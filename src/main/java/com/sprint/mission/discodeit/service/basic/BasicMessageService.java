package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AttachmentDTO;
import com.sprint.mission.discodeit.dto.CreateMessageDTO;
import com.sprint.mission.discodeit.dto.UpdateMessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(CreateMessageDTO createMessageDTO) {
        if (!channelRepository.existsById(createMessageDTO.getChannelId())) {
            throw new NoSuchElementException("Channel not found with channelId");
        }
        if (!userRepository.existsById(createMessageDTO.getUserId())) {
            throw new NoSuchElementException("Author not found with authorId");
        }

        Message message = new Message(createMessageDTO.getMessage(), createMessageDTO.getChannelId(), createMessageDTO.getUserId());
        messageRepository.save(message);
        BinaryContent binaryContent = binaryContentRepository.findByUserId(createMessageDTO.getUserId())
                .orElseThrow(()-> new NoSuchElementException("BinaryContent not found with userId"));
        // update가 안된다고 하기에 삭제하고 id만 set한 후에 다시 저장
        binaryContentRepository.delete(binaryContent);
        binaryContent.setMessageId(message.getId());
        binaryContentRepository.save(binaryContent);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public Message update(UpdateMessageDTO updateMessageDTO) {
        Message message = messageRepository.findById(updateMessageDTO.getMessageId())
                .orElseThrow(() -> new NoSuchElementException
                        ("Message with id " + updateMessageDTO.getMessageId() + " not found"));
        message.update(updateMessageDTO.getNewMessage());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        List<BinaryContent> allAttachments = binaryContentRepository.findAll();
        for(BinaryContent binaryContent : allAttachments) {
            if(messageId.equals(binaryContent.getMessageId())) {
                binaryContentRepository.delete(binaryContent);
            }
        }
        messageRepository.deleteById(messageId);
    }
}
