package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFMessageService(UserRepository userRepository, ChannelRepository channelRepository,
                             MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getUpdatedMessages() {
        return messageRepository.findUpdatedMessages();
    }

    @Override
    public void registerMessage(UUID channelId, String userName, String messageContent) {
        messageRepository.createMessage(channelRepository.findById(channelId), userRepository.findByName(userName),
                messageContent);
    }

    @Override
    public void updateMessage(UUID messageId, String messageContent) {
        messageRepository.updateMessage(messageId, messageContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }

}
