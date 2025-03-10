package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final String MESSAGE_FILE = "messages.ser";
    private final Map<UUID, Message> messageData;

    public FileMessageRepository() {
        messageData = loadData();
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
    public Message save(Message message) {
        messageData.put(message.getId(), message);
        saveData();

        return message;
    }

    @Override
    public Message findById(UUID id) {
        return messageData.get(id);
    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        messageData.remove(id);
        saveData();
    }
}
