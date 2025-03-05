package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final String FILE_NAME = "channels.dat";
    private Map<UUID, Channel> channels = new HashMap<>();

    public FileChannelService() {
        loadFromFile();
    }

    @Override
    public Channel create(ChannelType type,String name,  String description) {
        Channel channel = new Channel(UUID.randomUUID(), name, type, description);
        channels.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel update(UUID channelId, String newName, ChannelType newType, String newDescription) {
        Channel channel = channels.get(channelId);
        if (channel != null) {
            channel.updateName(newName);
            channel.setType(newType);
            channel.setDescription(newDescription);
            saveToFile();
        }
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        channels.remove(channelId);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            channels = (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            channels = new HashMap<>();
        }
    }
}
