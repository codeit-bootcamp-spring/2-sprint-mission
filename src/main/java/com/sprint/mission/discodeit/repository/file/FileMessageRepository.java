package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileMessageService;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private volatile static FileMessageRepository instance = null;
    private static final String FILE_PATH = "message.dat";
    private final Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data = loadAll();
    }

    public static FileMessageRepository getInstance() {
        if(instance == null){
            synchronized (FileMessageService.class){
                if(instance == null){
                    instance = new FileMessageRepository();
                }
            }
        }
        return instance;
    }

    private Map<UUID, Message> loadAll(){
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (Map<UUID, Message>) ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        saveToFile();
    }

    @Override
    public Message findById(UUID id) {
        return Optional.ofNullable(data.get(id)).orElseThrow(()-> new NoSuchElementException("Message not found"));
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }
}
