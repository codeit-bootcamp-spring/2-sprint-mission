package com.sprint.mission.discodeit.jcf;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessage implements MessageService {
    private final UserService userService;
    private final ChannelService channelService;

    HashMap<UUID, Message> messages = new HashMap<>();

    public JCFMessage(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message message) {
        userService
                .findById(message.getId())
                .orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        channelService
                .findById(message.getId())
                .orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        messages.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void update(UUID id) {

        Optional<Message> message = this.findById(id);

        if (message.isEmpty()) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }

        message.ifPresent(ch -> ch.setUpdatedAt(System.currentTimeMillis()));
    }

    @Override
    public void delete(UUID id) {
        messages.remove(id);
    }
}
