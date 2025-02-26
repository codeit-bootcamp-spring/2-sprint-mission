package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

// MessageService interface를 참조하여 기능을 구현한다.
public class JCFMessageService implements MessageService {
    Map<UUID, Message> messagemap = new HashMap<UUID, Message>();
    UserService userservice;
    ChannelService channelservice;

    public JCFMessageService(UserService userservice, ChannelService channelservice){
        this.userservice = userservice;
        this.channelservice = channelservice;
    }

    @Override
    public boolean CreateMessage(String message, String username, String channelname){
        UUID userid = null;
        UUID channelid = null;
        Map<UUID, User> temp = userservice.getAllUser();
        for(User eachuser : temp.values()){
            if(eachuser.getUsername().equals(username)){
                userid = eachuser.getId();
                break;
            }
        }
        Map<UUID, Channel> temp2 = channelservice.getAllChannel();
        for(Channel eachchannel : temp2.values()){
            if(eachchannel.getName().equals(channelname)){
                channelid = eachchannel.getId();
                break;
            }
        }
        if(userid == null || channelid == null){
            System.out.println("user or channel name is error");
            return false;
        }
        Message newmessage = new Message(message, userid, channelid);
        messagemap.put(newmessage.getId(), newmessage);
        return true;
    }

    @Override
    public Optional<Message> getMessage(UUID id){
        return Optional.ofNullable(messagemap.get(id));
    }

    @Override
    public Map<UUID, Message> getAllMessage(){
        return messagemap;
    }

    @Override
    public void UpdateMessage(String message, UUID id){
        messagemap.get(id).updateMessage(message);
    }

    @Override
    public void DeleteMessage(UUID id){
        messagemap.remove(id);
    }
}
