package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final String MESSAGE_FILE = "messages.ser";
    private final Map<UUID, Message> data;
    //
    private final ChannelService channelService;
    private final UserService userService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.data = loadData();
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
            oos.writeObject(this.data);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            channelService.find(channelId);
            userService.find(authorId);
        } catch (NoSuchElementException e){
            throw e;
        }
        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);
        saveData();

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.data.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message" + messageId + "가 존재하지 않습니다."));
    }

    @Override
    public List<Message> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.data.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message" + messageId + "가 존재하지 않습니다."));
        message.update(newContent);
        saveData();

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!this.data.containsKey(messageId)) {
            throw new NoSuchElementException("Message" + messageId + "가 존재하지 않습니다.");
        }
        this.data.remove(messageId);
    }
}
