package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private static final FileChannelService instance = new FileChannelService();
    private static final String FILE_PATH = "channels.ser";
    private final Map<UUID, Channel> data;

    private FileChannelService() {
        this.data = loadData();
    }

    public static FileChannelService getInstance() {
        return instance;
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Channel> loadData() {
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

    @Override
    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
        saveData();
        return channel;
    }

    @Override
    public Optional<Channel> getChannelById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public List<Channel> getChannelsByName(String name) {
        return data.values().stream()
                .filter(channel -> channel.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID channelId, String newName) {
        Channel channel = data.get(channelId);
        if (channel != null) {
            long updateTime = System.currentTimeMillis();
            channel.update(newName, updateTime);
            saveData();
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
        saveData();
    }
}
