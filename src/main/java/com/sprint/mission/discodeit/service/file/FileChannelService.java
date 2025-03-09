package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final String CHANNEL_FILE = "channels.ser";
    private final Map<UUID, Channel> data;

    public FileChannelService() {
        this.data = loadData();
    }

    private Map<UUID, Channel> loadData(){
        File file = new File(CHANNEL_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData(){
        try(FileOutputStream fos = new FileOutputStream(CHANNEL_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(this.data);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Channel create(ChannelType type, String name) {
        return null;
    }

    @Override
    public Channel find(UUID channelId) {
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public Channel update(UUID channelId, String newName, ChannelType newType) {
        return null;
    }

    @Override
    public void delete(UUID channelId) {

    }
}
