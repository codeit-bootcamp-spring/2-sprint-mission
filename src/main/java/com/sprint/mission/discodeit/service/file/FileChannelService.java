package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static final String FILE_NAME = "channel.ser";
    private Map<UUID, Channel> data;

    public FileChannelService() {
        this.data = loadFromFile();
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        data.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = data.get(channelId);
        return Optional.ofNullable(channel)
                .orElseThrow(() -> new NoSuchElementException("Channel with id" + channelId + "not found"));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = find(channelId);
        channel.update(newName, newDescription);
        saveToFile();
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id" + channelId + "not found");
        }
        data.remove(channelId);
        saveToFile();
    }


    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving channels data", e);
        }
    }

    private Map<UUID, Channel> loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading channels data", e);
        }
    }
}

