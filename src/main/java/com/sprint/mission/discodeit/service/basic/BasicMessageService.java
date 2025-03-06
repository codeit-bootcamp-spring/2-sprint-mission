package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicMessageService implements MessageService {

    private static MessageRepository messageRepository;
    private static ChannelRepository channelRepository;
    private static UserRepository userRepository;


    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message createMessage(UUID senderId, UUID channelId, String content) {
        Message message = new Message(senderId, channelId, content);
        Channel channel = channelRepository.findChannelById(channelId);

        channel.addMessages(message.getId());

        messageRepository.save(message);
        channelRepository.save(channel);
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messageRepository.findMessageById(messageId);
    }

    @Override
    public List<Message> findMessagesByUserAndChannel(UUID senderId, UUID channelId) {
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findChannelMessages(UUID channelId) {
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findUserMessages(UUID senderId) {
        return messageRepository.findMessageAll().stream()
                .filter(message -> message.getSenderId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findMessageById(messageId);
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findMessageById(messageId);
        Channel channel = channelRepository.findChannelById(message.getChannelId());

        channel.removeMessage(messageId);

        channelRepository.save(channel);
        messageRepository.deleteMessageById(messageId);

    }

    @Override
    public void validateMessage(UUID messageId) {
        if(!messageRepository.existsById(messageId)){
            throw new IllegalArgumentException("존재하지 않는 메세지입니다.");
        }
    }
}
