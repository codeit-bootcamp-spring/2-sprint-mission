package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.DTO.legacy.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository {

    void reset();
    Message save(Channel channel, Message message);

    Message find(UUID messageId);

    List<Message> findAllByChannelId(UUID channelId);

    List<Message> findAllByMessageId(UUID messageId);

    Message update(Message message, MessageCRUDDTO messageCRUDDTO);

    void remove(UUID messageId);
}
