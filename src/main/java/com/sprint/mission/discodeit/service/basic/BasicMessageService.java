package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelService channelService;
    private final UserService userService;


    private BasicMessageService(MessageRepository messageRepository, ChannelService channelService, UserService userService) {
        this.messageRepository = messageRepository;
        this.channelService = channelService;
        this.userService = userService;
    }

    private void saveMessageData() {
        messageRepository.save();
    }

    @Override
    public Message createMessage(UUID senderId, UUID channelId, String content) {
        Message message = new Message(senderId, channelId, content);
        Channel channel = channelService.findChannelById(channelId);

        channel.addMessages(message.getId());

        messageRepository.addMessage(message);
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        validateMessageExists(messageId);
        return messageRepository.findMessageById(messageId);
    }

    @Override
    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
        userService.validateUserExists(senderId);
        channelService.validateChannelExists(channelId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findChannelMessages(UUID channelId) {
        channelService.validateChannelExists(channelId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUserMessages(UUID senderId) {
        userService.validateUserExists(senderId);
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findMessageById(messageId);
        message.updateContent(newContent);
        saveMessageData();
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findMessageById(messageId);

        channelService.removeMessage(message.getChannelId(), messageId);

        messageRepository.deleteMessageById(messageId);
    }

    @Override
    public void validateMessageExists(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
        }
    }
}
