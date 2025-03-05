package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final String fileName = "channel.ser";
    private final Map<UUID, Channel> channelMap;

    public FileChannelService() {
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
    public Channel create(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        this.channelMap.put(channel.getId(), channel);
        saveChannelList();
        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channelNullable = this.channelMap.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
    }

    @Override
    public Channel update(UUID channelId, String newChannelName, String newDescription) {
        Channel channelNullable = this.channelMap.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId));
        channel.update(newChannelName, newDescription);
        saveChannelList();
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel removedChannel = this.channelMap.remove(channelId);
        if (removedChannel == null) {
            throw new NoSuchElementException("해당 채널을 찾을 수 없습니다 : " + channelId);
        }
    }
}
