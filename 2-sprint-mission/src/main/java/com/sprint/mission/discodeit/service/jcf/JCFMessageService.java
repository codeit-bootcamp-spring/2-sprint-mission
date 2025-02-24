package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
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
    public void createMessage(String messagestring){
        Message message = new Message(messagestring);
    }

    @Override
    public Message getMessageById(UUID id) {
        return data.get(id);
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
}
