package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final UserRepository jcfUserRepository;
    private final ChannelRepository jcfChannelRepository;
    private final MessageRepository jcfMessageRepository;

    public JCFMessageService(UserRepository jcfUserRepository, ChannelRepository jcfChannelRepository, MessageRepository jcfMessageRepository) {
        this.jcfUserRepository = jcfUserRepository;
        this.jcfChannelRepository = jcfChannelRepository;
        this.jcfMessageRepository = jcfMessageRepository;
    }

    @Override
    public void createMessage(User sender, String content, Channel channel) {
        UserService.validateUserId(sender.getId(), this.jcfUserRepository);
        ChannelService.validateChannelId(channel.getId(), this.jcfChannelRepository);
        Message newMessage = new Message(sender, content, channel);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        jcfMessageRepository.add(newMessage);
    }

    @Override
    public Message readMessage(UUID messageId) {
        return jcfMessageRepository.findById(messageId);
    }

    @Override
    public Map<UUID, Message> readAllMessages() {
        return jcfMessageRepository.getAll();
    }

    @Override
    public void updateMessageContent(UUID messageId, String content) {
        jcfMessageRepository.findById(messageId).updateContent(content);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        jcfMessageRepository.deleteById(messageId);
    }
}
