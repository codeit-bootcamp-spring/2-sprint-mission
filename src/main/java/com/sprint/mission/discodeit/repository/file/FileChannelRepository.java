package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final FileChannelRepository instance = new FileChannelRepository();
    private static final String FILE_PATH = "channels.ser";
    private final Map<UUID, Channel> data;

    private FileChannelRepository() {
        this.data = loadData();
    }

    public static FileChannelRepository getInstance() {
        return instance;
    }

    public void saveData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
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
