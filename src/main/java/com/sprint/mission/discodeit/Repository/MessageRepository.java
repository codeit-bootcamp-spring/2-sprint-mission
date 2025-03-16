package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository {

    void reset();
    void save(Channel channel, Message message);

    Message find(UUID messageId);

    List<Message> findAllByChannelId(UUID channelId);

    UUID update(Message message, MessageUpdateDTO messageUpdateDTO);

    void remove(Channel channel, Message message);
}
