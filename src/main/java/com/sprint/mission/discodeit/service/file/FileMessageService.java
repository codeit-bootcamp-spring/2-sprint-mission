package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final String MESSAGE_FILE = "messages.ser";
    private final Map<UUID, Message> messageData;
    //
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.messageData = loadData();
        this.channelService = channelService;
        this.userService = userService;
    }

    private Map<UUID, Message> loadData(){
        File file = new File(MESSAGE_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(){
        try(FileOutputStream fos = new FileOutputStream(MESSAGE_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this.messageData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message create(String writingMessage, UUID userId, UUID channelId) {
        try{
            channelService.find(channelId);
            userService.find(userId);
        }catch (NoSuchElementException e){
            throw new IllegalArgumentException("채널 혹은 유저가 존재하지 않습니다.");
        }

        Message message = new Message(writingMessage, userId, channelId);
        messageData.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId)).toList();
        if(messages.isEmpty() || messages == null){
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
        if(messages.isEmpty() || messages == null){
            throw new NoSuchElementException("채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        List<Message> messages = messageData.values().stream().filter(m -> m.getUserId().equals(userId) && m.getChannelId().equals(channelId)).toList();
        if (messages.isEmpty() || messages == null) {
            throw new NoSuchElementException("유저 " + userId + " 혹은 채널 " + channelId + "가 존재하지 않습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        Message messageNullable = messageData.get(messageId);
        Message message = Optional.ofNullable(messageNullable).orElseThrow(() -> new NoSuchElementException("메세지 " + messageId + "가 존재하지 않습니다."));
        message.updateMessage(newMessage);
        saveData();
        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if(!messageData.containsKey(messageId)){
            throw new NoSuchElementException("메세지 " + messageId + "가 존재하지 않습니다.");
        }
        messageData.remove(messageId);
        saveData();
    }
}
