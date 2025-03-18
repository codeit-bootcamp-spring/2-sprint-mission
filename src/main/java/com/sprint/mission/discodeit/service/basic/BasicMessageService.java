package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundException;
import com.sprint.mission.discodeit.Repository.BinaryContentRepository;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            messageRepository.reset();
        }
    }

    @Override
    public Message create(MessageCRUDDTO messageCRUDDTO) {
        UUID userId = messageCRUDDTO.creatorId();
        UUID channelUUID = messageCRUDDTO.channelId();

        User user = userRepository.find(userId);
        Channel channel = channelRepository.find(channelUUID);

        Message message = new Message(userId,user.getName(), channelUUID, messageCRUDDTO.text());

        if (messageCRUDDTO.binaryContent() != null) {
            List<UUID> attachmentIds = message.getAttachmentIds();
            List<BinaryContent> contentList = messageCRUDDTO.binaryContent();
            for (BinaryContent binaryContent : contentList) {
                attachmentIds.add(binaryContent.getBinaryContentId());
            }
        }

        messageRepository.save(channel, message);
        return message;
    }

    @Override
    public Message find(String messageId) {
        UUID messageUUID = UUID.fromString(messageId);

        Message message = messageRepository.find(messageUUID);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        List<Message> list = messageRepository.findAllByChannelId(channelUUID);
        return list;
    }

    @Override
    public void print(String channelId) {
        try {
            UUID channelUUID = UUID.fromString(channelId);

            Channel channel = channelRepository.find(channelUUID);
            System.out.println(channel.getName());

            List<Message> messages = messageRepository.findAllByChannelId(channel.getChannelId());
            for (Message message : messages) {
                System.out.println(message.getCreatorName() + " : " + message.getText());
            }
        } catch (NotFoundException e) {
            System.out.println("메시지 함이 비어있습니다.");
        }

    }

    @Override
    public boolean delete(MessageCRUDDTO messageCRUDDTO) {
        UUID channelId = messageCRUDDTO.channelId();
        UUID messageId = messageCRUDDTO.messageId();

        Channel channel = channelRepository.find(channelId);
        Message message = messageRepository.find(messageId);

        messageRepository.remove(channel, message);
        if (message.getAttachmentIds().isEmpty() == false) {
            List<UUID> attachmentIds = message.getAttachmentIds();
            for (UUID attachmentId : attachmentIds) {
                binaryContentRepository.delete(attachmentId);
            }
        }
        return true;
    }

    @Override
    public boolean update(String messageId, MessageCRUDDTO messageCRUDDTO) {
        UUID messageUUID = UUID.fromString(messageId);

        Message message = messageRepository.find(messageUUID);

        messageRepository.update(message, messageCRUDDTO);
        return true;
    }
}
