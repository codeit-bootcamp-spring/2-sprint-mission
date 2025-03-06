package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    UserRepository userRepository;
    ChannelRepository channelRepository;
    List<Message> messageList = new ArrayList<>();

    public JCFMessageRepository(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void messageSave(UUID channelUUID, UUID userUUID, String content) {
        Message message = new Message(userUUID, channelUUID, content);
        messageList.add(message);
    }
}
