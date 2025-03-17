package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;


public interface MessageService {
    void reset(boolean adminAuth);

    Message create(MessageCRUDDTO messageCRUDDTO);

    Message find(String messageId);

    List<Message> findAllByChannelId(String channelId);

    void print(String channelId);

    boolean delete(MessageCRUDDTO messageCRUDDTO);

    boolean update(String messageId, MessageCRUDDTO messageCRUDDTO);

}
