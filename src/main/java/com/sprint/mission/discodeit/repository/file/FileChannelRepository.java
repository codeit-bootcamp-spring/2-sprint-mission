package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String fileName = "channels.ser";
    private final Map<UUID, Channel> channelMap;

    public FileChannelRepository() {
        this.channelMap = loadChannelList();
    }

    public void saveChannelList() {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(channelMap);
        } catch (IOException e) {
            throw new RuntimeException("데이터를 저장하는데 실패했습니다.", e);
        }
    }

    public Map<UUID, Channel> loadChannelList() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object channelMap = ois.readObject();
            return (Map<UUID, Channel>) channelMap;
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("데이터를 불러오는데 실패했습니다", e);
        }
    }

    @Override
    public Channel save(Channel channel) {
        this.channelMap.put(channel.getId(), channel);
        saveChannelList();
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelMap.get(channelId));
    }

    @Override
    public Channel update(Channel channel) {
        this.channelMap.put(channel.getId(), channel);
        saveChannelList();
        return channel;
    }

    @Override
    public boolean delete(UUID channelId) {
        boolean removed = channelMap.remove(channelId) != null;
        if (removed) {
            saveChannelList();
        }
        return removed;
    }
}
