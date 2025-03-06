package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private volatile static FileChannelRepository instance = null;
    private static final String FILE_PATH = "channel.dat";
    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = loadAll();
    }

    public static FileChannelRepository getInstance(){
        if(instance == null){
            synchronized (FileChannelRepository.class){
                if(instance == null){
                    instance = new FileChannelRepository();
                }
            }
        }
        return instance;
    }

    private Map<UUID, Channel> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
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
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        saveToFile();
    }

    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(data.get(id)).orElseThrow(()-> new NoSuchElementException("Channel not found"));
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }
}
