package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.infra.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;

    public JCFMessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public MessageDto create(String context, UUID channelId, UUID userId) {
        Message message = messageRepository.save(
                new Message(context, channelId, userId)
        );

        return new MessageDto(message.getId(), message.getContext(), message.getChannelId(),
                userService.findById(userId));
    }

    @Override
    public MessageDto findById(UUID id) {
        Message message = messageRepository.findById(id);

        return new MessageDto(message.getId(), message.getContext(), message.getChannelId(),
                userService.findById(message.getUserId()));
    }

    @Override
    public List<MessageDto> findAll() {
        return messageRepository.findAll()
                .stream()
                .map(message -> new MessageDto(message.getId(), message.getContext(), message.getChannelId(),
                        userService.findById(message.getUserId())))
                .toList();
    }

    @Override
    public List<MessageDto> findByChannelId(UUID channelId) {
        return messageRepository.findAll()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(message -> new MessageDto(message.getId(), message.getContext(), message.getChannelId(),
                        userService.findById(message.getUserId())))
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        messageRepository.updateContext(id, context);
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
