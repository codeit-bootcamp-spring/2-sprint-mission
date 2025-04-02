package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
    private final String filePath;
    private final Map<UUID, Channel> data;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String baseDir) {
        this.filePath = baseDir + "/channels.ser";
        this.data = loadData();
    }

    public void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        }
    }

    public Map<UUID, Channel> loadData() {
        File file = new File(filePath);
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
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Channel> getChannelById(UUID ChannelId) {
        return Optional.ofNullable(data.get(ChannelId));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteChannel(UUID ChannelId) {
        data.remove(ChannelId);
        try {
            saveData();
        } catch (IOException e) {
            throw new RuntimeException("채널 삭제 후 저장 중 오류 발생", e);
        }
    }
}
