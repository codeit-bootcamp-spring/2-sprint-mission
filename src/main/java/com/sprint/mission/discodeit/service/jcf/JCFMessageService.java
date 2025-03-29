package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messageData;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageData = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }


    @Override
    public Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentDTO> binaryContentDtos) {
        try{
            channelService.find(messageCreateRequest.channelId());
            userService.find(messageCreateRequest.userId());
        }catch (NoSuchElementException e){
            throw new IllegalArgumentException("채널 혹은 유저가 존재하지 않습니다.");
        }

//        Message message = messageCreateRequest.toEntity();
//        messageData.put(message.getId(), message);
//        return message;
        return null;
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId)).toList();
        if(messages.isEmpty()){
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
        if(messages.isEmpty()){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId) && m.getChannelId().equals(channelId)).toList();
        if (messages.isEmpty()) {
            throw new NoSuchElementException("유저 " + userId + " 혹은 채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }

    @Override
    public Message update(MessageUpdateRequest messageUpdateRequest) {
        Message messageNullable = messageData.get(messageUpdateRequest.messageId());
        Message message = Optional.ofNullable(messageNullable).orElseThrow(() -> new NoSuchElementException("메세지 " + messageUpdateRequest.messageId() + "가 존재하지 않습니다."));
        message.updateMessage(messageUpdateRequest.newMessage());
        return message; 
    }

    @Override
    public void delete(UUID messageId) {
        if(!messageData.containsKey(messageId)){
            throw new NoSuchElementException("메세지 " + messageId + "가 존재하지 않습니다.");
        }
        messageData.remove(messageId);
    }
}





