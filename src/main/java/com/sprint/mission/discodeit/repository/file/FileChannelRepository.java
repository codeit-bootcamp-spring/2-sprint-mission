package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();
    private static final String FILE_NAME = "channel.ser";

    public FileChannelRepository() {
        loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getUuid(), channel);
        saveToFile();
        return data.get(channel.getUuid());
    }

    @Override
    public void delete(UUID channelKey) {
        data.remove(channelKey);
        saveToFile();
    }

    @Override
    public Channel findByKey(UUID channelKey) {
        return data.get(channelKey);
    }

    @Override
    public boolean existsByKey(UUID channelKey) {
        return data.containsKey(channelKey);
    }

    @Override
    public Channel findByName(String name) {
        return data.values().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> findAllByNames(List<String> names) {
        return data.values().stream()
                .filter(c -> names.contains(c.getName()))
                .toList();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("[Error] 사용자 데이터를 저장하는 중 문제가 발생했습니다.");
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
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
