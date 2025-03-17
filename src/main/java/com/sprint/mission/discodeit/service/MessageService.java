package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Message.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;


public interface MessageService {
    void reset(boolean adminAuth);

    Message create(MessageDTO messageDTO);

    Message find(String messageId);

    List<Message> findAllByChannelId(String channelId);

    void print(String channelId);

    boolean delete(MessageDTO messageDTO);

    boolean update(String messageId, MessageDTO messageDTO);

}
