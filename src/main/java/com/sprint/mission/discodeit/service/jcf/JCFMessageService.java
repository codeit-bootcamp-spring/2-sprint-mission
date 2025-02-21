package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

//User, Channel 검증 로직 추가
public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;


    // UserService, ChannelService를 의존성 주입(DI)
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void create(Message message) {
        boolean userExists = userService.read(message.getUserId()).isPresent();
        boolean channelExists = channelService.read(message.getChannelId()).isPresent();

        if (!userExists) {
            System.out.println("❌User " + message.getUserId() + " does not exist❌");
            return;
        }
        if (!channelExists) {
            System.out.println("❌Channel " + message.getChannelId() + " does not exist❌");
            return;
        }

        data.put(message.getId(), message);
        System.out.println("✅메세지 전송 완료!");
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
