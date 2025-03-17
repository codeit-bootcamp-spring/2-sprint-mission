package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private Map<UUID, Channel> channelData;
    private static final String CHANNEL_FILE_PATH = "channels.ser";

    public FileChannelService() {
        dataLoad();
    }

    private void dataLoad() {
        File file = new File(CHANNEL_FILE_PATH);
        if (!file.exists()) {
            channelData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(CHANNEL_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channelData = (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    private void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHANNEL_FILE_PATH))) {
            oos.writeObject(channelData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 저장할 수 없습니다.");
        }
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        if(channelData.values().stream()
                .anyMatch(channel -> channel.getName().equals(name))){
            throw new IllegalArgumentException("같은 이름을 가진 채널이 있습니다.");
        }
        Channel channel = new Channel(type, name, description);
        this.channelData.put(channel.getId(), channel);

        dataSave();
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = this.channelData.get(channelId);
        return Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return this.channelData.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channelNullable = this.channelData.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(newName, newDescription);

        dataSave();
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!this.channelData.containsKey(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        this.channelData.remove(channelId);

        dataSave();
    }
}
