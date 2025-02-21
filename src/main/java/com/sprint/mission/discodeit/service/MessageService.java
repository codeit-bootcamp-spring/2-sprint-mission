package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;


public interface MessageService {
    void createMessage(Message message);
    Message getMessage(UUID id);
    List<Message> getAllMessages();
    void updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
}