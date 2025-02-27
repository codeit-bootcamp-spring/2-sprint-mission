package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<UUID, Message>();
    }

    @Override
    public void createMessage(String newmessage, User user, Channel channel){
        Message message = new Message(newmessage, user, channel);

        data.put(message.getId(), message);
    }

    @Override
    public Message getMessageById(UUID id) {
        return data.get(id);
    }

    public Message getMessageByUser(User user){
        for (Message message : data.values()) {
            if (message.getUser().equals(user)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages(){
        return new ArrayList<Message>(data.values());
    }

    @Override
    public Message updateMessage(UUID id, String message){
        Message newmessage = data.get(id).updateMessage(message);
        data.put(id, newmessage);
        return newmessage;
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }

    public boolean isDeleted(UUID id) {
        return data.containsKey(id);
    }
}
