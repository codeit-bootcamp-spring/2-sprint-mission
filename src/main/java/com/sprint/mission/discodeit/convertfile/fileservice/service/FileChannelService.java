package com.sprint.mission.discodeit.convertfile.fileservice.service;

import com.sprint.mission.discodeit.convertfile.entity.Channel;
import com.sprint.mission.discodeit.convertfile.entity.ChannelType;
import com.sprint.mission.discodeit.convertfile.fileservice.ChannelService;
import java.util.*;
import java.io.*;


public class FileChannelService implements ChannelService {
    private Map<UUID, Channel> data;
    private final String FILE_PATH = "channels.dat";

    public FileChannelService() {
        loadFromFile();
    }

    // 파일에서 데이터 로드
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object readObject = ois.readObject();
            if (readObject instanceof HashMap) {
                this.data = (HashMap<UUID, Channel>) readObject;
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우 무시하고 새로운 Map 사용
            this.data = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading channel data: " + e.getMessage());
            this.data = new HashMap<>();
        }
    }

    // 파일에 데이터 저장
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.data);
        } catch (IOException e) {
            System.err.println("Error saving channel data: " + e.getMessage());
        }
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        this.data.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(this.data.values());
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channelNullable = this.data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);
        saveToFile();
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!this.data.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        this.data.remove(channelId);
        saveToFile();
    }
}