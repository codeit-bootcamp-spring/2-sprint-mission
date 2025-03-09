package com.sprint.mission.discodeit.service.file;

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
    public Message create(String message, UUID userId, UUID channelId) {
        return null;
    }

    @Override
    public List<Message> findByUser(UUID userId) {
        return List.of();
    }

    @Override
    public List<Message> findByChannel(UUID channelId) {
        return List.of();
    }

    @Override
    public List<Message> findByUserAndByChannel(UUID userId, UUID channelId) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public Message update(UUID messageId, String newMessage) {
        return null;
    }

    @Override
    public void delete(UUID messageId) {

    }
}
