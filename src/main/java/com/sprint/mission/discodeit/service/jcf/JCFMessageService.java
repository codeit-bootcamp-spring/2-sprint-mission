package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

// MessageService interface를 참조하여 기능을 구현한다.
public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messagemap = new HashMap<UUID, Message>();
    UserService userservice;
    ChannelService channelservice;

    public JCFMessageService(UserService userservice, ChannelService channelservice){
        this.userservice = userservice;
        this.channelservice = channelservice;
    }

    @Override
    public void createMessage(String message, UUID userid, UUID channelid){
        Message newmessage = new Message(message, userid, channelid);
        messagemap.put(newmessage.getId(), newmessage);
    }

    @Override
    public Optional<Message> getOneMessage(UUID id){
        return Optional.ofNullable(messagemap.get(id));
    }

    @Override
    public List<Message> getAllMessage(){
        List<Message> messageList = new ArrayList<>(messagemap.values());
        return messageList;
    }

    @Override
    public void updateMessage(String message, UUID id){
        messagemap.get(id).updateMessage(message);
    }

    @Override
    public void deleteMessage(UUID id){
        messagemap.remove(id);
    }
}
