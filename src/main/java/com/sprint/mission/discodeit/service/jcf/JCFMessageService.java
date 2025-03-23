package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final JCFMessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageRepository = new JCFMessageRepository();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(MessageDTO messageDTO) {
        userService.getUser(messageDTO.senderId());
        channelService.getChannel(messageDTO.channelId());

        Message message = new Message(
                messageDTO.senderId(),
                messageDTO.channelId(),
                messageDTO.content()
        );
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = getMessage(id);
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID id) {
        getMessage(id);
        messageRepository.delete(id);
    }
}
