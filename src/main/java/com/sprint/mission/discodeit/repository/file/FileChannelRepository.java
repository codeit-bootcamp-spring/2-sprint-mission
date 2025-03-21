package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();
    private static final String FILE_NAME = "channel.ser";
    private final String filePath;
    public FileChannelRepository(String directory) {
        this.filePath = directory + "/" + FILE_NAME;
        loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getUuid(), channel);
        saveToFile();

        return data.get(channel.getUuid());
    }

    @Override
    public Channel findByKey(UUID channelKey) {
        return data.get(channelKey);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Channel> findAllByKeys(List<UUID> channelKeys) {
        return channelKeys.stream()
                .map(data::get)
                .toList();
    }

    @Override
    public void delete(UUID channelKey) {
        data.remove(channelKey);
        saveToFile();
    }

    @Override
    public boolean existsByKey(UUID channelKey) {
        return data.containsKey(channelKey);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof UUID key && entry.getValue() instanceof Channel value) {
                        data.put(key, value);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] 사용자 데이터를 불러오는 중 문제가 발생했습니다.");
        }
    }
}
