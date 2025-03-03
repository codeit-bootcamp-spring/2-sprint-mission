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

import java.util.LinkedList;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFMessageService(UserRepository userRepository, ChannelRepository channelRepository ,MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(User sender, String content, Channel channel) {
        UserService.validateUserId(sender.getId(), this.userRepository);
        ChannelService.validateChannelId(channel.getId(), this.channelRepository);
        Message newMessage = new Message(sender, content, channel);     //content에 대한 유효성 검증은 Message 생성자에게 맡긴다.
        messageRepository.addMessage(newMessage);
    }

    @Override
    public Message readMessage(UUID messageId) {
        MessageService.validateMessageId(messageId, this.messageRepository);            //아래ㅐ 코드와 작동 과정 중복, 일관성을 챙길 것인가? 코드 중복을 줄일 것 인가?
        return messageRepository.findMessageByMessageId(messageId);
    }

    @Override
    public LinkedList<Message> readAllMessages() {
        return messageRepository.getMessages();
    }

    @Override
    public void updateMessageContent(UUID messageId, String content) {
        MessageService.validateMessageId(messageId, this.messageRepository);     //아래 코드와 작동 과정 중복, 일관성을 챙길 것인가? 코드 중복을 줄일 것 인가?
        messageRepository.findMessageByMessageId(messageId).updateContent(content);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }
}
