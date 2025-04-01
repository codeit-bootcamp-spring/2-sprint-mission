package com.sprint.sprint2.discodeit.service.jcf;

import com.sprint.sprint2.discodeit.entity.Message;
import com.sprint.sprint2.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.sprint2.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.sprint2.discodeit.service.ChannelService;
import com.sprint.sprint2.discodeit.service.MessageService;
import com.sprint.sprint2.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final JCFChannelRepository jcfChannelRepository;
    private final JCFMessageRepository jcfMessageRepository;
    private final UserService userService;

    public JCFMessageService(JCFMessageRepository jcfMessageRepository, UserService userService, JCFChannelRepository channelRepository) {
        this.jcfChannelRepository = channelRepository;
        this.jcfMessageRepository = jcfMessageRepository;
        this.userService = userService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        jcfChannelRepository.findById(channelId.toString());
        userService.find(authorId);
        Message message = new Message(content, channelId, authorId);
        jcfMessageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return jcfMessageRepository.findById(messageId.toString());
    }

    @Override
    public List<Message> findAll() {
        return jcfMessageRepository.findByAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = jcfMessageRepository.findById(messageId.toString());
        message.update(newContent);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
      jcfMessageRepository.delete(messageId);
    }
}
