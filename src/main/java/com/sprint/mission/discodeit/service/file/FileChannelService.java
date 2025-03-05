package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        Map<UUID, Channel> channels = loadChannels();
        channels.put(channel.getId(), channel);
        saveChannels(channels);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        Channel channel = channels.get(channelId);
        if (channel == null) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadChannels().values());
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Map<UUID, Channel> channels = loadChannels();
        Channel channel = channels.get(channelId);
        if (channel == null) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channel.update(newName, newDescription);
        saveChannels(channels);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> channels = loadChannels();
        if (!channels.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channels.remove(channelId);
        saveChannels(channels);
    }

    @Override
    public boolean exists(UUID channelId) {
        return false;
    }

    private Map<UUID, Channel> loadChannels() {
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

    private void saveChannels(Map<UUID, Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
