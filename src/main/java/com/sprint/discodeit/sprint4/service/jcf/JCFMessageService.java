package com.sprint.discodeit.sprint4.service.jcf;

import com.sprint.discodeit.sprint5.domain.entity.Message;
import com.sprint.discodeit.sprint4.repository.jcf.JCFChannelRepository;
import com.sprint.discodeit.sprint4.repository.jcf.JCFMessageRepository;
import com.sprint.discodeit.sprint4.service.MessageService;
import com.sprint.discodeit.sprint4.service.usersService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final JCFChannelRepository jcfChannelRepository;
    private final JCFMessageRepository jcfMessageRepository;
    private final usersService usersService;

    public JCFMessageService(JCFMessageRepository jcfMessageRepository, usersService usersService, JCFChannelRepository channelRepository) {
        this.jcfChannelRepository = channelRepository;
        this.jcfMessageRepository = jcfMessageRepository;
        this.usersService = usersService;
    }

    @Override
    public void create(String content, UUID channelId, UUID authorId) {
//        Channel channel = jcfChannelRepository.findById(channelId).orElseThrow(() -> new IllegalArgumentException("없는 채널 입니다."));
//        users users = usersService.find(authorId);
//        Message message = new Message(content,channel.getId(), authorId);
//        jcfMessageRepository.save(message);

    }

    @Override
    public Message find(UUID messageId) {
        return jcfMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));
    }

    @Override
    public List<Message> findAll() {
        return jcfMessageRepository.findByAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = jcfMessageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(messageId + " 없는 회원 입니다"));
        message.update(newContent);
        return message;
    }

    @Override
    public void delete(UUID messageId) {
      jcfMessageRepository.delete(messageId);
    }
}
