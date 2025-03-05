package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private static JCFMessageService jsfMessageService;

    private final UserService userService;
    private final ChannelService channelService;


    HashMap<UUID, Message> messages = new HashMap<>();

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
        if(jsfMessageService == null){
            jsfMessageService = new JCFMessageService(userService, channelService);
        }

        return jsfMessageService;
    }

    @Override
    public void create(Message message, UUID channelId, UUID authorId) {
        userService
                .findById(authorId)
                .orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        channelService
                .findById(channelId)
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
    public void update(UUID id, String content, UUID channelId, UUID authorId) {
        Message message = this.findById(id).orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        message.setContent(content);
        message.setChannelId(channelId);
        message.setAuthorId(authorId);
    }

    @Override
    public void delete(UUID id) {
        if (!messages.containsKey(id)) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }
        messages.remove(id);
    }
}
