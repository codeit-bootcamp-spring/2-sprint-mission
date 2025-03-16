package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.Message.MessageCreateDTO;
import com.sprint.mission.discodeit.DTO.Message.MessageIDSDTO;
import com.sprint.mission.discodeit.DTO.Message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;


public interface MessageService {
    void reset(boolean adminAuth);

    Message create(MessageCreateDTO messageCreateDTO);


    Message find(String messageId);

    List<Message> findAllByChannelId(String channelId);


    void print(String channelId);


    boolean delete(MessageIDSDTO messageIDSDTO);


    boolean update(String messageId, MessageUpdateDTO messageUpdateDTO);

}
