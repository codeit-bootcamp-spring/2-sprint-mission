package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final String CHANNEL_FILE = "channels.ser";
    private final Map<UUID, Channel> channelData;

    public FileChannelRepository() {
        channelData = loadData();
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
            oos.writeObject(this.channelData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public Channel save(Channel channel) {
        channelData.put(channel.getId(), channel);
        saveData();
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return channelData.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        channelData.remove(id);
        saveData();
    }
}
