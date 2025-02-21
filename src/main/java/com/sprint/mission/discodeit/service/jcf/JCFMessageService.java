package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private volatile static JCFMessageService instance = null;
    private final UserService userService;
    private final ChannelService channelService;
    private final Map<UUID, Message> messageRepository;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageRepository = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if (instance == null) {
            synchronized (JCFMessageService.class) {
                if(instance == null) {
                    instance = new JCFMessageService(userService, channelService);
                }
            }
        }
        return instance;
    }

    @Override
    public Message saveMessage(String text) {
        Message message = new Message(text);
        messageRepository.put(message.getId(), message);
        return message;
    }

    @Override
    public void findAll() {
        messageRepository.values().stream()
                .findFirst()
                .ifPresentOrElse(
                        message -> messageRepository.values().forEach(System.out::println),
                        () -> System.out.println("등록된 메세지가 없습니다.")
                );
    }

    @Override
    public void findById(UUID id) {
        Optional<Message> message = Optional.ofNullable(messageRepository.get(id));

        message.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("해당 id의 메세지가 없습니다.")
        );
    }

    @Override
    public void update(UUID id, String text) {
        Optional<Message> message = Optional.ofNullable(messageRepository.get(id));

        message.ifPresentOrElse(
                msg -> msg.setText(text),
                () -> System.out.println("해당 id의 메세지가 없습니다.")
        );
    }

    @Override
    public void delete(UUID id) {
        messageRepository.remove(id);
    }
}
