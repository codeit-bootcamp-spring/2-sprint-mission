package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private JCFUserService userService;
    private JCFChannelService channelService;


    public void createMessage(Message message) {
        if(userService.getUser(message.getUserId()) != null && channelService.getChannel(message.getChannelId()) != null)
        {
            data.put(message.getId(), message);
        }

    }
    public Message getMessage(UUID id) { return data.get(id); }
    public List<Message> getAllMessages() { return new ArrayList<>(data.values()); }
    public void updateMessage(UUID id, String content) {
        Message messageToUpdate = getMessage(id);
        if (data.containsKey(id)&& messageToUpdate != null && userService.getUser(messageToUpdate.getUserId()) != null)
            data.get(id).updateContent(content);
    }
    public void deleteMessage(UUID id) { data.remove(id); }
}
