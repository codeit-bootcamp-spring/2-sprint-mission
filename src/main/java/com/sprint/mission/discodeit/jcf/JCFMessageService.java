package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
     private final Map<UUID, Message> messageData;
     private static JCFMessageService messageInstance;
     private final JCFChannelService channelService;
     private final JCFUserService userService;

     public static JCFMessageService getInstance(){
         if(messageInstance == null){
             messageInstance = new JCFMessageService();
         }
         return messageInstance;
     }

     private JCFMessageService() {
         messageData = new HashMap<>();
         channelService = JCFChannelService.getInstance();
         userService = JCFUserService.getInstance();
     }

     public void containsMessage(UUID mid) {
        if(!(messageData.containsKey(mid))){
            throw new IllegalArgumentException("존재하지 않는 메시지ID입니다.");
        }
     }

     @Override
     public Message getMessage(UUID mid){
        containsMessage(mid);
        return messageData.get(mid);
     }

     @Override
     public List<Message> getAllMessage(){
         if(messageData.isEmpty()){
             throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
         }
         return new ArrayList<>(messageData.values());
     }

     public List<Message> getUpdatedMessages(){
         return messageData.values().stream()
                 .filter(entry-> entry.getMessageUpdateAt() != null)
                 .collect(Collectors.toList());
     }

     @Override
     public void registerMessage(UUID cid, String userName, String messageContent){
         Message message = new Message(channelService.getChannel(cid), userService.getUser(userName), messageContent);
         this.messageData.put(message.getMid(), message);
     }

     @Override
     public void updateMessage(UUID mid, String messageContent){
         containsMessage(mid);
         Message message = this.messageData.get(mid);
         message.messageUpdate(messageContent);
     }

     @Override
     public void deleteMessage(UUID mid){
         containsMessage(mid);
         this.messageData.remove(mid);
     }
}
