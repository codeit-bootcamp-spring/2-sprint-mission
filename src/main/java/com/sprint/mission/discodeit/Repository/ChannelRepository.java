package com.sprint.mission.discodeit.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void saveMessage(Message message);

    Message findMessageByChannel(Channel channel, UUID messageId);

    List<Message> findMessageListByChannel(Channel channel);

    List<Message> findMessageListByChannel(UUID channelId);

    UUID updateMessage(Channel channel, Message message, String replaceText);

    UUID removeMessage(Channel channel,  Message message);
}
